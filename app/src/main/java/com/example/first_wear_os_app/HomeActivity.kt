package com.example.first_wear_os_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val btnSendLocation = findViewById<Button>(R.id.send_location)
        btnSendLocation.setOnClickListener {
            sendPetition()
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://apimocha.com/retrofit2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun sendPetition() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).postData("example/")
            val response = call.body()
            if (call.isSuccessful){
                Log.i("response", response.toString())
            }
        }

    }



}