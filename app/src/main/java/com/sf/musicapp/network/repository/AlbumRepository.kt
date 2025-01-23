package com.sf.musicapp.network.repository

import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Track

interface AlbumRepository {
    suspend fun getRecommendedAlbums(page:Int):List<Album>
    suspend fun searchAlbums(query:String,page:Int):List<Album>
    suspend fun getAlbumById(id:String): List<Album>

    suspend fun getTrackByAlbumId(id:String,page:Int):List<Track>
    suspend fun getAllTrackById(id:String): List<Track>
}