package com.example.first_wear_os_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.first_wear_os_app.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timer().schedule(4000){
            navigateToHome()
            finish()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(
            this,
            HomeActivity::class.java
        )
        startActivity(intent)
    }
}