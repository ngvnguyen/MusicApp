package com.sf.musicapp.view.activity.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sf.musicapp.adapter.source.BasePagingSource
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.network.repository.AlbumRepository
import com.sf.musicapp.network.repository.ArtistRepository
import com.sf.musicapp.network.repository.PlaylistRepository
import com.sf.musicapp.network.repository.TrackRepository
import com.sf.musicapp.utils.Limits
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val trackRepository: TrackRepository,
    private val albumRepository: AlbumRepository,
    private val playlistRepository: PlaylistRepository
): ViewModel() {
    private val pagingConfig = PagingConfig(
        Limits.PAGE_SIZE,
        Limits.PAGE_SIZE,
        true,
        Limits.PAGE_SIZE,
        Limits.PAGE_SIZE*499
    )
    val artistRecommendedPager = Pager(pagingConfig){ BasePagingSource<Artist> { artistRepository.getRecommendedArtist(it) } }
        .flow.cachedIn(viewModelScope)

    val trackRecommendedPager = Pager(pagingConfig){ BasePagingSource<Track> { trackRepository.getRecommendedTrack(it) } }
        .flow.cachedIn(viewModelScope)

    val albumRecommendedPager = Pager(pagingConfig){
        BasePagingSource<Album>{albumRepository.getRecommendedAlbums(it)}
    }.flow.cachedIn(viewModelScope)

    val playlistRecommendedPager = Pager(pagingConfig){
        BasePagingSource<Playlist>{playlistRepository.getPlaylists(it)}
    }.flow.cachedIn(viewModelScope)
}
