package com.mfkw.compass.model

import android.location.Location
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinates(val latitude: Double, val longitude: Double) : Parcelable

fun Location.toCoordinates() = Coordinates(latitude, longitude)
