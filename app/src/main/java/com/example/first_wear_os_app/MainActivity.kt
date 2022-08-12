package com.example.first_wear_os_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.first_wear_os_app.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnToHome = findViewById<Button>(R.id.to_home)
        btnToHome.setOnClickListener {
            navigateToHome()
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