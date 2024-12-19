package com.sf.musicapp.network.repository.implement

import com.sf.musicapp.data.model.Track
import com.sf.musicapp.network.api.TrackApi
import com.sf.musicapp.network.model.toTrack
import com.sf.musicapp.network.repository.TrackRepository
import com.sf.musicapp.utils.Limits

class TrackRepositoryImpl(private val trackApi: TrackApi): TrackRepository {
    override suspend fun searchTrack(
        query: String,
        page: Int
    ): List<Track> {
        return trackApi.searchTrack(query, offset = page* Limits.PAGE_SIZE).results.map { it->it.toTrack() }
    }

    override suspend fun getRecommendedTrack(page: Int): List<Track> {
        return trackApi.getRecommendedTrack(offset = page* Limits.PAGE_SIZE).results.map { it->it.toTrack() }
    }

    override suspend fun getTrackById(id: String): List<Track> {
        return trackApi.getTrackById(id).results.map { it->it.toTrack() }
    }
}