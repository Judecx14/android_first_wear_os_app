package com.example.first_wear_os_app

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.timerTask

class HomeActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var btnSendLocation: Button
    private lateinit var txtLength: TextView
    private lateinit var txtLatitude: TextView
    private var length = 0.0
    private var latitude = 0.0
    private val toastText = "Location sent!"
    private val toastDuration = Toast.LENGTH_SHORT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        txtLength = findViewById(R.id.length)
        txtLatitude = findViewById(R.id.latitude)
        btnSendLocation = findViewById(R.id.send_location)
        btnSendLocation.setOnClickListener {
            getCurrentLocation()
            sendPetition()
        }
        Timer().scheduleAtFixedRate(timerTask {
            getCurrentLocation()
        }, 4000, 1)
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val location: Location = it.result
            txtLength.text = location.longitude.toString()
            txtLatitude.text = location.latitude.toString()
            length = location.longitude
            latitude = location.latitude
            Log.i("location", location.toString())
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
            val data = Location(length, latitude)
            val call = getRetrofit().create(APIService::class.java).postData("example/", data)
            val response = call.body()
            if (call.isSuccessful) {
                makeToast()
                Log.i("response", response.toString())
            }
        }

    }

    private suspend fun makeToast(){
        withContext(Dispatchers.Main) {
            val toast = Toast.makeText(applicationContext, toastText, toastDuration)

            toast.show()
        }
    }

}