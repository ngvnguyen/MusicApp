package com.sf.musicapp.network.api

import com.sf.musicapp.network.model.AlbumResponse
import com.sf.musicapp.network.model.PlaylistResponse
import com.sf.musicapp.utils.Jamendo
import com.sf.musicapp.utils.Limits
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistApi {
    @GET("/v3.0/playlists")
    suspend fun getPlaylists(
        @Query("offset") offset:Int=0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): PlaylistResponse

    @GET("/v3.0/playlists")
    suspend fun searchPlaylists(
        @Query("namesearch") query:String,
        @Query("offset") offset:Int=0,
        @Query("limit") limit:Int = Limits.PAGE_SIZE,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): PlaylistResponse

    @GET("/v3.0/playlists")
    suspend fun getPlaylistById(
        @Query("id") id:String,
        @Query("client_id") clientId:String = Jamendo.CLIENT_ID,
        @Query("format") format:String = Jamendo.JSON
    ): PlaylistResponse
}