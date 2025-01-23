package com.sf.musicapp.network.repository

import com.sf.musicapp.data.model.Playlist

interface PlaylistRepository {
    suspend fun getPlaylists(page:Int):List<Playlist>
    suspend fun searchPlaylists(query:String,page:Int):List<Playlist>
    suspend fun getPlaylistsById(id:String):List<Playlist>
}