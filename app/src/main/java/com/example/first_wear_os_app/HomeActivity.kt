package com.example.first_wear_os_app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var btnSendLocation: Button
    private lateinit var txtLength: TextView
    private lateinit var txtLatitude: TextView
    private var length = 0.0
    private var latitude = 0.0
    private val toastText = "Location sent!"
    private val toastDuration = Toast.LENGTH_SHORT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getCurrentLocation()
                } else {
                    val toast =
                        Toast.makeText(applicationContext, "Need permissions", Toast.LENGTH_LONG)
                    toast.show()
                    requestPermissonLocation()
                }
            }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        setContentView(R.layout.activity_home)
        txtLength = findViewById(R.id.length)
        txtLatitude = findViewById(R.id.latitude)
        btnSendLocation = findViewById(R.id.send_location)
        btnSendLocation.setOnClickListener {
            sendPetition()
        }
    }


    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissonLocation()
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 4000, 0F
            ) { location ->
                txtLength.text = location.longitude.toString()
                txtLatitude.text = location.latitude.toString()
                length = location.longitude
                latitude = location.latitude
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://167.99.5.117/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun sendPetition() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissonLocation()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val data = Location(length, latitude)
                val call = getRetrofit().create(APIService::class.java).postData("coords", data)
                if (call.isSuccessful) {
                    makeToast()
                }
            }
        }
    }

    private suspend fun makeToast() {
        withContext(Dispatchers.Main) {
            val toast = Toast.makeText(applicationContext, toastText, toastDuration)
            toast.show()
        }
    }

    private fun requestPermissonLocation() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            99
        )
    }

}