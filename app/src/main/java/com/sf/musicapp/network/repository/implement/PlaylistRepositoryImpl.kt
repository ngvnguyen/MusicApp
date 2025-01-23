package com.sf.musicapp.network.repository.implement

import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.network.api.PlaylistApi
import com.sf.musicapp.network.model.toPlaylist
import com.sf.musicapp.network.repository.PlaylistRepository
import com.sf.musicapp.utils.Jamendo
import com.sf.musicapp.utils.Limits

class PlaylistRepositoryImpl(private val playlistApi: PlaylistApi): PlaylistRepository {
    override suspend fun getPlaylists(page: Int): List<Playlist> {
        return playlistApi.getPlaylists(page* Limits.PAGE_SIZE).results
            .map { it.toPlaylist() }
    }

    override suspend fun searchPlaylists(
        query: String,
        page: Int
    ): List<Playlist> {
        return playlistApi.searchPlaylists(query,page* Limits.PAGE_SIZE).results
            .map { it.toPlaylist() }
    }

    override suspend fun getPlaylistsById(id: String): List<Playlist> {
        return playlistApi.getPlaylistById(id).results.map { it.toPlaylist() }
    }

}