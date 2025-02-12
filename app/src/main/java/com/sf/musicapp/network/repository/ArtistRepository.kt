package com.sf.musicapp.network.repository

import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Track

interface ArtistRepository {
    suspend fun getRecommendedArtist(page: Int): List<Artist>
    suspend fun searchArtist(query:String,page:Int): List<Artist>
    suspend fun getArtistById(id:String): List<Artist>

    suspend fun getTrackByArtistId(id:String,page:Int):List<Track>
    suspend fun getAllTrackById(id:String): List<Track>
    suspend fun getAlbumsByArtistId(id:String): List<Album>
}
