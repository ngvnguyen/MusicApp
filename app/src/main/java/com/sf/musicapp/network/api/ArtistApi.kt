package com.sf.musicapp.network.api

import com.sf.musicapp.network.model.AlbumTrackResponse
import com.sf.musicapp.network.model.ArtistAlbumResponse
import com.sf.musicapp.network.model.ArtistResponse
import com.sf.musicapp.network.model.ArtistTrackResponse
import com.sf.musicapp.utils.Jamendo
import com.sf.musicapp.utils.Limits
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtistApi {
    @GET("/v3.0/artists")
    suspend fun searchArtist(
        @Query("namesearch") query:String,
        @Query("offset") offset:Int = 0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("format") format:String = Jamendo.JSON,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID
    ): ArtistResponse

    @GET("/v3.0/artists")
    suspend fun getRecommendedArtists(
        @Query("order") order:String = Jamendo.OrderBy.PopularityWeek.code,
        @Query("offset") offset:Int = 0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("format") format:String = Jamendo.JSON,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID
    ): ArtistResponse

    @GET("/v3.0/artists")
    suspend fun getArtistById(
        @Query("id") id:String,
        @Query("format") format:String = Jamendo.JSON,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID
    ): ArtistResponse

    @GET("/v3.0/artists/tracks")
    suspend fun getTrackByArtistId(
        @Query("id") id:String,
        @Query("offset") offset:Int=0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): ArtistTrackResponse

    @GET("/v3.0/artists/tracks")
    suspend fun getAllTrackByArtistId(
        @Query("id") id:String,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): ArtistTrackResponse


    @GET("/v3.0/artists/albums")
    suspend fun getAlbumsByArtistId(
        @Query("id") id:String,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): ArtistAlbumResponse

}