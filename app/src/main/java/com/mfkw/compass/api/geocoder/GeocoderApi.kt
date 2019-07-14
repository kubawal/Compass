package com.mfkw.compass.api.geocoder

import com.mfkw.compass.api.geocoder.model.GeocoderPlace
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocoderApi {
    @GET("search.php?format=json")
    suspend fun queryPlaces(@Query("q") query: String): List<GeocoderPlace>

    @GET("reverse.php?&format=json")
    suspend fun queryCoordinates(@Query("lat") lat: Double, @Query("lon") lon: Double): GeocoderPlace
}
