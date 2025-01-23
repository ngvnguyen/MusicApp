package com.sf.musicapp.view.fragment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sf.musicapp.adapter.source.BasePagingSource
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.network.repository.AlbumRepository
import com.sf.musicapp.utils.Limits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class AlbumPickerViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
): ViewModel(){
    private var albumId:String?= null


    private val pagingConfig = PagingConfig(
        Limits.PAGE_SIZE,
        Limits.PAGE_SIZE,
        true,
        Limits.PAGE_SIZE,
        Limits.PAGE_SIZE*499
    )

    var trackPager : Flow<PagingData<Track>> = flowOf()
        private set




    suspend fun getAllTrack():List<Track>{
        try{
            val result = albumRepository.getAllTrackById(albumId!!)
            return result
        }catch (e: Exception){
            Log.d("AlbumPickerViewModel",e.message.toString())
            return listOf()
        }

    }

    fun setAlbumId(albumId:String){
        this.albumId = albumId
        trackPager = Pager(pagingConfig){ BasePagingSource<Track>{
            try{
                albumRepository.getTrackByAlbumId(albumId,it)
            }catch (e: Exception){
                Log.d("AlbumPickerViewModel",e.message.toString())
                listOf()
            }
        }}.flow.cachedIn(viewModelScope)
    }

}