package com.mfkw.compass.service

import android.content.Context
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.model.toCoordinates
import com.mfkw.compass.util.await

class LocationService(
    private val locationProvider: FusedLocationProviderClient,
    private val context: Context
) {
    private var currentCallback: LocationCallback? = null

    fun startLocationUpdates(callback: (Coordinates) -> Unit) {
        val locationCallback = locationCallbackFor(callback)
        locationProvider.requestLocationUpdates(
            createLocationRequest(),
            locationCallback,
            null
        )
        currentCallback = locationCallback
    }

    fun stopLocationUpdates() {
        if (currentCallback != null) {
            locationProvider.removeLocationUpdates(currentCallback)
            currentCallback = null
        }
    }

    suspend fun ensureLocationEnabled(): ResolvableApiException? {
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(createLocationRequest())
            .build()

        return runCatching {
            LocationServices
                .getSettingsClient(context)
                .checkLocationSettings(settingsRequest)
                .await()
        }.exceptionOrNull() as? ResolvableApiException
    }

    private fun createLocationRequest() =
        LocationRequest().apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    private fun locationCallbackFor(callback: (Coordinates) -> Unit) = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            if (result != null)
                callback(result.lastLocation.toCoordinates())
        }
    }
}
