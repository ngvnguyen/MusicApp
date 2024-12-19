package com.sf.musicapp.network.repository

import com.sf.musicapp.data.model.Track

interface TrackRepository {
    suspend fun searchTrack(
        query:String,
        page:Int
    ): List<Track>

    suspend fun getRecommendedTrack(page: Int): List<Track>

    suspend fun getTrackById(id: String): List<Track>

}