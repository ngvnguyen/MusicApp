package com.sf.musicapp.network.repository

import com.sf.musicapp.data.model.Artist

interface ArtistRepository {
    suspend fun getRecommendedArtist(page: Int): List<Artist>
    suspend fun searchArtist(query:String,page:Int): List<Artist>
    suspend fun getArtistById(id:String): List<Artist>
}
