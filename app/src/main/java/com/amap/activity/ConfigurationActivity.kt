package com.amap.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import com.amap.R
import com.amap.dao.CountryDao
import com.amap.database.AppDatabase
import com.amap.entity.Country

class ConfigurationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "user_database"
        ).build()

        this.findViewById<Button>(R.id.save).setOnClickListener {
            val nickname = this.findViewById<EditText>(R.id.nickname).text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("nickname", nickname)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        this.findViewById<Button>(R.id.populateDatabase).setOnClickListener {
            populateDb(db.countryDao())
            this.findViewById<Button>(R.id.populateDatabase).isEnabled = false
        }
    }

    private fun populateDb(dao: CountryDao) {
        Thread {
            var file = readFile("geocountries.txt")
            val countries = mutableListOf<Country>()
            var countriesStr = file.split("\n").toList()

            countriesStr.forEach {
                val c = it.split(":")

                if(c.size > 1) {
                    countries.add(
                        Country(
                            name = c[0],
                            capital = c[1],
                            longitude = c[2].toFloat(),
                            latitude = c[3].toFloat(),
                            continent = c[4],
                            population = null,
                            flag = null,
                            currency = null,
                            id = 0
                        )
                    )
                }
            }

            file = readFile("geo.data.txt")
            countriesStr = file.split("\n").toList()

            countriesStr.forEach{
                val c = it.split(":")

                if(c.size > 1 ) {

                    val country = countries.find { it.name == c.get(0) }
                    if(country != null){
                        countries.remove(country)
                        countries.add(
                            Country(
                                name = country.name,
                                capital = country.capital,
                                longitude = country.longitude,
                                latitude = country.latitude,
                                continent = country.continent,
                                population = c[2].toInt(),
                                flag = null,
                                currency = c[3],
                                id = 0
                            )
                        )
                    }
                }
            }

            dao.insertAll(*countries.toTypedArray())
        }.start()
    }

    private fun readFile(filename: String): String {
        val inputStream = assets.open(filename)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        return String(buffer)
    }
}