package com.sf.musicapp.network.api

import com.sf.musicapp.data.model.Album
import com.sf.musicapp.network.model.AlbumResponse
import com.sf.musicapp.network.model.AlbumTrackResponse
import com.sf.musicapp.utils.Jamendo
import com.sf.musicapp.utils.Limits
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumApi {
    @GET("/v3.0/albums")
    suspend fun getRecommendedAlbums(
        @Query("offset") offset:Int=0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("order") order:String = Jamendo.OrderBy.PopularityTotal.code,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): AlbumResponse

    @GET("/v3.0/albums")
    suspend fun searchAlbums(
        @Query("namesearch") query:String,
        @Query("offset") offset:Int=0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): AlbumResponse

    @GET("/v3.0/albums")
    suspend fun getAlbumById(
        @Query("id") id:String,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): AlbumResponse

    @GET("/v3.0/albums/tracks")
    suspend fun getTrackByAlbumId(
        @Query("id") id:String,
        @Query("offset") offset:Int=0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): AlbumTrackResponse

    @GET("/v3.0/albums/tracks")
    suspend fun getAllTrackByAlbumId(
        @Query("id") id:String,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): AlbumTrackResponse
}