package com.sf.musicapp.network.repository

import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.data.model.Track

interface PlaylistRepository {
    suspend fun getPlaylists(page:Int):List<Playlist>
    suspend fun searchPlaylists(query:String,page:Int):List<Playlist>
    suspend fun getPlaylistsById(id:String):List<Playlist>

    suspend fun getTrackByPlaylistId(id:String,page:Int):List<Track>
    suspend fun getAllTrackById(id:String): List<Track>
}