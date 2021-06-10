package com.amap.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.room.Room
import com.amap.R
import com.amap.database.AppDatabase
import com.amap.entity.Country
import kotlin.random.Random

class PlayGameActivity : AppCompatActivity() {

    private lateinit var header: TextView
    private lateinit var question: TextView
    private var nickname = ""

    private val answersButtons = mutableListOf<Button>()
    private lateinit var correctAnswer: Button

    private var score = 0
    private var bestScore = 0

    private lateinit var countries: MutableList<Country>

    private lateinit var db: AppDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)

        nickname = intent.getStringExtra("nickname").toString()

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        bestScore = sharedPref.getInt("$nickname", 0)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "user_database"
        ).build()

        header = findViewById(R.id.header)
        updateHeader()
        question = findViewById(R.id.question)

        answersButtons.add(this.findViewById(R.id.answer1))
        answersButtons.add(this.findViewById(R.id.answer2))
        answersButtons.add(this.findViewById(R.id.answer3))
        answersButtons.add(this.findViewById(R.id.answer4))

        answersButtons.forEach {it.setOnClickListener{
            check(it as Button)
        }}

        fetchCountries()
        if(countries.isEmpty()) {
            question.text = "Database is empty, populate database in configuration."
            answersButtons.forEach {it.isVisible = false}
        }
        else
            nextQuestion()
    }

    override fun onResume() {
        super.onResume()
        //nickname = intent.getStringExtra("nickname").toString()

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        bestScore = sharedPref.getInt("$nickname", 0)
        updateHeader()
    }

    override fun onDestroy() {
        super.onDestroy()

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val bestScore = sharedPref.getInt("$nickname", 0)
        if(score > bestScore) {
            with(sharedPref.edit()) {
                putInt("$nickname", score)
                apply()
            }
        }
    }

    private fun check(button: Button) {

        if(correctAnswer == button){
            score++
            updateHeader()
            nextQuestion()
        }
        else{
            button.isEnabled = false
        }
    }

    private fun nextQuestion() {
        answersButtons.forEach { it.isEnabled = true }

        if(countries.isEmpty()) fetchCountries()

        val correctButNum = Random.nextInt(0,4)
        correctAnswer = answersButtons.get(correctButNum)

        val num = Random.nextInt(0,3)

        when (num) {
            0 -> {
                val correct = countries.random()
                countries.remove(correct)
                question.text = "What is the capital of ${correct.name}?"
                correctAnswer.setText(correct.capital)
                answersButtons.forEach {
                    if(it != correctAnswer){
                        it.setText(countries.random().capital)
                    }
                }
            }
            1 -> {
                var correct = countries.random()
                while(correct.currency == null)
                    correct = countries.random()
                countries.remove(correct)
                question.text = "What is the currency of ${correct.name}?"
                correctAnswer.setText(correct.currency)
                answersButtons.forEach {
                    if(it != correctAnswer){
                        var ans = countries.random()
                        while(ans.currency == null || ans.currency == correct.currency)
                            ans = countries.random()
                        it.setText(ans.currency)
                    }
                }
            }
            2 -> {
                var correct = countries.random()
                while(correct.population == null)
                    correct = countries.random()
                countries.remove(correct)
                question.text = "What is the population of ${correct.name}?"
                correctAnswer.setText(correct.population.toString())
                answersButtons.forEach {
                    if(it != correctAnswer){
                        var ans = countries.random()
                        while(ans.population == null || ans.population == correct.population)
                            ans = countries.random()
                        it.setText(ans.population.toString())
                    }
                }
            }
        }


    }

    private fun updateHeader() {
        header.text = "Nickname: $nickname \nPoints: $score \nBest score: $bestScore"
    }

    private fun fetchCountries() {
        val t = Thread{
            countries = db.countryDao().getAll()
        }
        t.start()
        t.join()
    }
}