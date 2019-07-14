package com.mfkw.compass.configuration

import com.mfkw.compass.api.geocoder.GeocoderApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val GEOCODER_API_KEY = "3046abfcfcc268"
private const val GEOCODER_BASE_URL = "https://eu1.locationiq.com/v1/"

val geocoderModule = Kodein.Module("geocoder") {
    bind<Interceptor>(tag = "geocoder") with singleton { createInterceptor() }
    bind<OkHttpClient>(tag = "geocoder") with singleton { createHttpClient(instance(tag = "geocoder")) }
    bind<Converter.Factory>() with singleton { GsonConverterFactory.create() }
    bind<Retrofit>() with singleton { createRetrofit(instance(tag = "geocoder"), instance()) }
    bind<GeocoderApi>() with singleton { createGeocoderApi(instance()) }
}

private fun createInterceptor() = Interceptor {
    val url = it.request()
        .url()
        .newBuilder()
        .addQueryParameter("key", GEOCODER_API_KEY)
        .build()

    val request = it.request()
        .newBuilder()
        .url(url)
        .build()

    it.proceed(request)
}

private fun createHttpClient(interceptor: Interceptor) =
    OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

private fun createRetrofit(httpClient: OkHttpClient, converterFactory: Converter.Factory) =
    Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(converterFactory)
        .baseUrl(GEOCODER_BASE_URL)
        .build()

private fun createGeocoderApi(retrofit: Retrofit) = retrofit.create<GeocoderApi>()
