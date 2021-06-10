package com.amap.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.amap.R


class MainActivity : AppCompatActivity() {

    val NICKNAME = "nickname"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.findViewById<Button>(R.id.playGame).setOnClickListener {
            val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
            val nickname = sharedPref.getString(NICKNAME, "")
            val intent = Intent(this, PlayGameActivity::class.java).apply {

                putExtra(NICKNAME, nickname)
            }
            startActivity(intent)
        }

        this.findViewById<Button>(R.id.configuration).setOnClickListener {
            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivityForResult(intent, 1)
        }

        this.findViewById<Button>(R.id.quit).setOnClickListener {
            this.finishAffinity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode === Activity.RESULT_OK) {
                    val nickname = data?.getStringExtra(NICKNAME).toString()
                    val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
                    with(sharedPref.edit()) {
                        putString(NICKNAME, nickname)
                        apply()
                    }
                }
            }
        }
    }
}