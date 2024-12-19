package com.sf.musicapp.view.activity.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sf.musicapp.adapter.source.BasePagingSource
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.network.repository.ArtistRepository
import com.sf.musicapp.network.repository.TrackRepository
import com.sf.musicapp.utils.Limits
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val trackRepository: TrackRepository
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



}
