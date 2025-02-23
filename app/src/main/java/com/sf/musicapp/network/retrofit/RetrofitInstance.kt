package com.sf.musicapp.network.retrofit

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sf.musicapp.network.api.AlbumApi
import com.sf.musicapp.network.api.ArtistApi
import com.sf.musicapp.network.api.PlaylistApi
import com.sf.musicapp.network.api.TrackApi
import com.sf.musicapp.utils.isNetworkAvailable
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File

class RetrofitInstance(private val context: Context) {
    val BASE_URL="https://api.jamendo.com"
    val cacheSize = (10*1024*1024).toLong()
    val cache = Cache(File(context.filesDir,"request_cache"),cacheSize)
    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor { chain->
            var request = chain.request()
            request = if (context.isNetworkAvailable()){
                request.newBuilder()
                    .header("Cache-Control","public, max-age= 86400")
                    .build()
            } else{
                    request.newBuilder()
                        .header("Cache-Control","public, only-if-cached, max-stale=604800")
                        .build()
            }

            chain.proceed(request)
        }
        .cache(cache)
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
    val albumApi by lazy{
        retrofit.create(AlbumApi::class.java)
    }
    val playlistApi by lazy{
        retrofit.create(PlaylistApi::class.java)
    }

}

