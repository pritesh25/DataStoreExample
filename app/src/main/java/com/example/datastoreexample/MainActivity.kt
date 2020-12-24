package com.example.datastoreexample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mTag = "MainActivity"
    private var settingsManager: SettingManager? = null
    private var isDarkMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsManager = SettingManager(applicationContext)
        initViews()
        observeUiPreferences()

    }

    private fun initViews() {
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            Log.d(mTag, "clicked")
            setTheme()
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            setPersonName()
        }
    }

    private fun setPersonName() {
        lifecycleScope.launch {
            settingsManager?.setName(findViewById<EditText>(R.id.editTextTextPersonName).text!!.toString())
        }
    }

    private fun setTheme() {
        lifecycleScope.launch {
            when (isDarkMode) {
                true -> {
                    Log.d(mTag, "light clicked")
                    settingsManager?.setUiMode(UiMode.LIGHT)
                }
                false -> {
                    Log.d(mTag, "dark clicked")
                    settingsManager?.setUiMode(UiMode.DARK)
                }
            }
        }
    }

    private fun observeUiPreferences() {

        settingsManager?.uiTheme?.asLiveData()?.observe(this) { uiMode ->
            when (uiMode) {
                UiMode.LIGHT -> {
                    Log.d(mTag, "light observe")
                    isDarkMode = false
                    findViewById<ConstraintLayout>(R.id.mainLayout).setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                }
                UiMode.DARK -> {
                    Log.d(mTag, "dark observe")
                    isDarkMode = true
                    findViewById<ConstraintLayout>(R.id.mainLayout).setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                }
                else -> {
                    println("nothing observe")
                }
            }
        }

        settingsManager?.personName?.asLiveData()?.observe(this) {
            it?.let { s ->
                findViewById<EditText>(R.id.editTextTextPersonName).setText(s)
            }
        }

    }

}