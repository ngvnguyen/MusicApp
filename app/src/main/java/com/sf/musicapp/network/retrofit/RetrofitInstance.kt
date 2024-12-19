package com.sf.musicapp.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sf.musicapp.network.api.ArtistApi
import com.sf.musicapp.network.api.TrackApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitInstance {
    const val BASE_URL="https://api.jamendo.com"
    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()
    private val json = Json{
        ignoreUnknownKeys = true
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val artistApi by lazy{
        retrofit.create(ArtistApi::class.java)
    }
    val trackApi by lazy {
        retrofit.create(TrackApi::class.java)
    }


}