package com.sf.musicapp.network.repository.implement

import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.network.api.ArtistApi
import com.sf.musicapp.network.model.toArtist
import com.sf.musicapp.network.repository.ArtistRepository
import com.sf.musicapp.utils.Limits

class ArtistRepositoryImpl(private val artistApi: ArtistApi): ArtistRepository {
    override suspend fun getRecommendedArtist(page: Int): List<Artist> {
        return artistApi.getRecommendedArtists(offset = page* Limits.PAGE_SIZE)
            .results.map { it->it.toArtist() }
    }

    override suspend fun searchArtist(query: String,page:Int): List<Artist> {
        return artistApi.searchArtist(query=query, offset = Limits.PAGE_SIZE)
            .results.map { it->it.toArtist() }
    }

    override suspend fun getArtistById(id: String): List<Artist> {
        return artistApi.getArtistById(id)
            .results.map { it->it.toArtist() }
    }
}