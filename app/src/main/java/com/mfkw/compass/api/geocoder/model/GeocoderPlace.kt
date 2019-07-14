package com.mfkw.compass.api.geocoder.model

import com.google.gson.annotations.SerializedName

data class GeocoderPlace(
    val lat: Double,
    val lon: Double,
    @SerializedName("display_name") val displayName: String,
    val type: String
)
