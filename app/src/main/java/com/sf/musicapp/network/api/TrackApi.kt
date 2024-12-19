package com.sf.musicapp.network.api

import com.sf.musicapp.network.model.TrackResponse
import com.sf.musicapp.utils.Jamendo
import com.sf.musicapp.utils.Limits
import retrofit2.http.GET
import retrofit2.http.Query


interface TrackApi {
    @GET("/v3.0/tracks")
    suspend fun getTrackById(
        @Query("id") id:String,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): TrackResponse

    @GET("/v3.0/tracks")
    suspend fun getRecommendedTrack(
        @Query("order") order: String = Jamendo.OrderBy.PopularityWeek.code,
        @Query("offset") offset:Int=0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): TrackResponse

    @GET("/v3.0/tracks")
    suspend fun searchTrack(
        @Query("namesearch") query:String,
        @Query("offset") offset:Int = 0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("format") format:String = Jamendo.JSON,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID
    ): TrackResponse
}