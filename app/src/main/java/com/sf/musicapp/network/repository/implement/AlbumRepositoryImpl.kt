package com.sf.musicapp.network.repository.implement

import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.network.api.AlbumApi
import com.sf.musicapp.network.model.toAlbum
import com.sf.musicapp.network.model.toListTrack
import com.sf.musicapp.network.repository.AlbumRepository
import com.sf.musicapp.utils.Limits

class AlbumRepositoryImpl(private val albumApi: AlbumApi): AlbumRepository {
    override suspend fun getRecommendedAlbums(page: Int): List<Album> {
        return albumApi.getRecommendedAlbums(Limits.PAGE_SIZE*page).results
            .map { it.toAlbum() }
    }

    override suspend fun searchAlbums(query: String,page:Int): List<Album> {
        return albumApi.searchAlbums(query,page* Limits.PAGE_SIZE).results
            .map { it.toAlbum() }
    }

    override suspend fun getAlbumById(id: String): List<Album> {
        return albumApi.getAlbumById(id).results
            .map { it.toAlbum() }
    }

    override suspend fun getTrackByAlbumId(id: String,page:Int): List<Track> {
        val results = mutableListOf<Track>()
        albumApi.getTrackByAlbumId(id,page*Limits.PAGE_SIZE).results.forEach{
            results.addAll(it.toListTrack())
        }
        return results
    }

    override suspend fun getAllTrackById(id: String): List<Track> {
        val results = mutableListOf<Track>()
        albumApi.getAllTrackByAlbumId(id).results.forEach{
            results.addAll(it.toListTrack())
        }
        return results
    }


}