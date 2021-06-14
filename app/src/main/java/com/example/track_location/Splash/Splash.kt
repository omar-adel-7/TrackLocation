package com.example.track_location.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.track_location.Main.MainActivity
import com.example.track_location.databinding.ActSplashBinding
import com.general.base_act_frg.BaseAppCompatActivity

class Splash : BaseAppCompatActivity() {
    private val SPLASH_DISPLAY_LENGHT = 1200
    lateinit var binding: ActSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startApp()
    }

    private fun startApp() {
        runOnUiThread {
            Handler().postDelayed({
                val myIntent = Intent(this@Splash, MainActivity::class.java)
                startActivity(myIntent)
                finishAffinity()
                overridePendingTransition(0, 0)
            }, SPLASH_DISPLAY_LENGHT.toLong())
        }
    }
}