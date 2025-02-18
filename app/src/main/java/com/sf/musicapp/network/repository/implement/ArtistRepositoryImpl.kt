package com.sf.musicapp.network.repository.implement

import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.network.api.ArtistApi
import com.sf.musicapp.network.model.toAlbum
import com.sf.musicapp.network.model.toArtist
import com.sf.musicapp.network.model.toListAlbum
import com.sf.musicapp.network.model.toListTrack
import com.sf.musicapp.network.repository.ArtistRepository
import com.sf.musicapp.utils.Limits

class ArtistRepositoryImpl(private val artistApi: ArtistApi): ArtistRepository {
    override suspend fun getRecommendedArtist(page: Int): List<Artist> {
        return artistApi.getRecommendedArtists(offset = page* Limits.PAGE_SIZE)
            .results.map { it->it.toArtist() }
    }

    override suspend fun searchArtist(query: String,page:Int): List<Artist> {
        return artistApi.searchArtist(query=query, offset = Limits.ITEM_SEARCH)
            .results.map { it->it.toArtist() }
    }

    override suspend fun getArtistById(id: String): List<Artist> {
        return artistApi.getArtistById(id)
            .results.map { it->it.toArtist() }
    }

    override suspend fun getTrackByArtistId(
        id: String,
        page: Int
    ): List<Track> {
        val result = mutableListOf<Track>()
        artistApi.getTrackByArtistId(id,page* Limits.PAGE_SIZE).results.forEach{result.addAll(it.toListTrack())}
        return result
    }

    override suspend fun getAllTrackById(id: String): List<Track> {
        val result = mutableListOf<Track>()
        artistApi.getAllTrackByArtistId(id).results.forEach{result.addAll(it.toListTrack())}
        return result
    }

    override suspend fun getAlbumsByArtistId(id: String): List<Album> {
        val result = mutableListOf<Album>()
        artistApi.getAlbumsByArtistId(id).results.forEach{result.addAll(it.toListAlbum())}
        return result
    }
}