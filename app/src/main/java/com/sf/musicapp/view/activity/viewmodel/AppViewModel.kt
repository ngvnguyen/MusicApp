package com.sf.musicapp.view.activity.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val trackRepository: TrackRepository,
    private val albumRepository: AlbumRepository,
    private val playlistRepository: PlaylistRepository
): ViewModel() {
    private var tracks : List<Track> = listOf()

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

    private val _query = MutableStateFlow<String>("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val trackSearchPager =
        _query.flatMapLatest {query->
            if (query.length > 1) getSearchTrackPager(query) else flowOf()
        }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val albumSearchPager =
        _query.flatMapLatest {query->
            if (query.length > 1) getSearchAlbumPager(query) else flowOf()
        }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val artistSearchPager =
        _query.flatMapLatest { query->
            if (query.length > 1) getSearchArtistPager(query) else flowOf()
        }.cachedIn(viewModelScope)


    @OptIn(ExperimentalCoroutinesApi::class)
    val playlistSearchPager =
        _query.flatMapLatest { query->
            if (query.length > 1) getSearchPlaylistPager(query) else flowOf()
        }.cachedIn(viewModelScope)

    /**
    *setTrackPager để dùng
     */
    var trackSetPager: Flow<PagingData<Track>> = flowOf()

    fun setTracksPager(tracks: List<Track>){
        this.tracks = tracks
        trackSetPager = Pager(pagingConfig){
            BasePagingSource<Track>{getPageTrack(it)}
        }.flow.cachedIn(viewModelScope)
    }
    private fun getPageTrack(page: Int): List<Track>{
        return tracks.drop(page*Limits.PAGE_SIZE).take(Limits.PAGE_SIZE)
    }

    suspend fun getAllTrackByAlbumId(albumId: String):List<Track>{
        try{
            val result = albumRepository.getAllTrackById(albumId)
            return result
        }catch (e: Exception){
            Log.d("AlbumPickerViewModel",e.message.toString())
            return listOf()
        }

    }
    suspend fun getAllTrackByArtistId(artistId: String):List<Track>{
        try{
            val result = artistRepository.getAllTrackById(artistId)
            return result
        }catch (e: Exception){
            Log.d("AlbumPickerViewModel",e.message.toString())
            return listOf()
        }

    }

    suspend fun getAllAlbumByArtistId(artistId: String):List<Album>{
        try{
            val result = artistRepository.getAlbumsByArtistId(artistId)
            return result
        }catch (e: Exception){
            Log.d("AlbumPickerViewModel",e.message.toString())
            return listOf()
        }

    }

    suspend fun getAllTrackByPlaylistId(playlistId: String):List<Track>{
        try{
            val result = playlistRepository.getAllTrackById(playlistId)
            return result
        }catch (e: Exception) {
            Log.d("AlbumPickerViewModel", e.message.toString())
            return listOf()
        }
    }

    fun getSearchTrackPager(query:String):Flow<PagingData<Track>>{
        return Pager(pagingConfig){
            BasePagingSource<Track>(){
                trackRepository.searchTrack(query,it)
            }
        }.flow.cachedIn(viewModelScope)
    }
    fun getSearchAlbumPager(query:String):Flow<PagingData<Album>>{
        return Pager(pagingConfig){
            BasePagingSource<Album>(){
                albumRepository.searchAlbums(query,it)
            }
        }.flow.cachedIn(viewModelScope)
    }
    fun getSearchArtistPager(query: String):Flow<PagingData<Artist>>{
        return Pager(pagingConfig){
            BasePagingSource<Artist>(){
                artistRepository.searchArtist(query,it)
            }
        }.flow.cachedIn(viewModelScope)
    }
    fun getSearchPlaylistPager(query: String):Flow<PagingData<Playlist>>{
        return Pager(pagingConfig){
            BasePagingSource<Playlist>(){
                playlistRepository.searchPlaylists(query,it)
            }
        }.flow.cachedIn(viewModelScope)
    }

    fun search(query:String){
        _query.value = query
    }

}
