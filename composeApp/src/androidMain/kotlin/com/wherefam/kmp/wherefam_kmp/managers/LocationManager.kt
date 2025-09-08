package com.wherefam.kmp.wherefam_kmp.managers

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@SuppressLint("MissingPermission")
class LocationManager(
    context: Context
) {

//    private val androidLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun getLocation(
        onSuccess: (latitude: Double, longitude: Double) -> Unit,
    ){
//        val executor = Executors.newSingleThreadExecutor()
//        androidLocationManager.getCurrentLocation(
//            LocationManager.GPS_PROVIDER,
//            null,
//            executor
//        ) { location ->
//            if (location != null) {
//                onSuccess(location.latitude, location.longitude)
//            }
//        }

        fusedLocationClient
            .lastLocation
            .addOnSuccessListener { location ->
                onSuccess(location.latitude, location.longitude)
            }
    }

    fun trackLocation(): Flow<Location> {
        return callbackFlow {
//            val locationListener = LocationListener { location ->
//                launch {
//                    send(location)
//                }
//            }
//
//            val updateInterval = 2000L
//            val minDistance = 10f
//
//            androidLocationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                updateInterval,
//                minDistance,
//                locationListener,
//                Looper.getMainLooper()
//            )
//
//            awaitClose {
//                androidLocationManager.removeUpdates(locationListener)
//            }

            val locationCallback = locationCallback { location ->
                launch {
                    send(location)
                }
            }

            val request = LocationRequest
                .Builder(1000)
                .build()

            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    private fun locationCallback(
        onResult: (location: Location) -> Unit,
    ): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)

                result.lastLocation?.let { location ->
                    onResult(location)
                }
            }
        }
    }
}