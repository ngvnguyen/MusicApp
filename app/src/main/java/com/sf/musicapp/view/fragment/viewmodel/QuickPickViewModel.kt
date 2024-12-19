package com.sf.musicapp.view.fragment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sf.musicapp.MusicApp
import com.sf.musicapp.adapter.source.BasePagingSource
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.network.api.ArtistApi
import com.sf.musicapp.network.model.toArtist
import com.sf.musicapp.network.repository.ArtistRepository
import com.sf.musicapp.network.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class QuickPickViewModel(private val artistRepository: ArtistRepository): ViewModel() {


}



