package com.sf.musicapp.view.activity.viewmodel

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.data.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortType{
    NAME,DATE,NONE
}
enum class SortOrder{
    ASC,DESC
}
data class SortState(
    val sortType: SortType,
    val sortOrder: SortOrder
)

@HiltViewModel
class DBViewModel @Inject constructor(
    private val appDatabaseRepository: DatabaseRepository
): ViewModel() {
    private var trackScope:Job? = null
    private var albumScope:Job? = null
    private var artistScope:Job? = null
    private var playlistScope:Job? = null

    var tracks: StateFlow<List<Track>> = flowOf<List<Track>>().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        listOf())

    var albums: StateFlow<List<Album>> = flowOf<List<Album>>().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        listOf())

    var artists: StateFlow<List<Artist>> = flowOf<List<Artist>>().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        listOf())

    var playlists: StateFlow<List<Playlist>> = flowOf<List<Playlist>>().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        listOf())

    private val _trackIsFavourite = MutableStateFlow<Boolean>(false)
    val trackIsFavourite = _trackIsFavourite as StateFlow<Boolean>

    private val _albumIsSaved = MutableStateFlow<Boolean>(false)
    val albumIsSaved = _albumIsSaved as StateFlow<Boolean>

    private val _artistIsSaved = MutableStateFlow<Boolean>(false)
    val artistIsSaved = _artistIsSaved as StateFlow<Boolean>

    private val _playlistIsSaved = MutableStateFlow<Boolean>(false)
    val playlistIsSaved = _playlistIsSaved as StateFlow<Boolean>

    private val _sortState = MutableStateFlow<SortState>(
        SortState(SortType.NONE, SortOrder.DESC))
    val sortState = _sortState as StateFlow<SortState>

    init {
        viewModelScope.launch{
            _sortState.collectLatest {
                when(it){
                    SortState(SortType.NAME,SortOrder.ASC)->sortByName(true)
                    SortState(SortType.NAME,SortOrder.DESC)->sortByName(false)
                    SortState(SortType.DATE,SortOrder.ASC)->sortByDate(true)
                    SortState(SortType.DATE,SortOrder.DESC)->sortByDate(false)
                    SortState(SortType.NONE,SortOrder.ASC)->noSort()
                    SortState(SortType.NONE,SortOrder.DESC)->noSort()
                }
            }
        }
    }

    fun setSortType(sortType: SortType){
        _sortState.update { it.copy(sortType = sortType) }
    }
    fun toggleSortOrder(){
        _sortState.update {
            it.copy(sortOrder = if (it.sortOrder == SortOrder.ASC)
                SortOrder.DESC else SortOrder.ASC)
        }
    }

    fun setTrackId(trackId:String){
        trackScope?.cancel()
        trackScope = viewModelScope.launch{
            appDatabaseRepository.countTrackById(trackId).stateIn(viewModelScope).collectLatest {
                _trackIsFavourite.value = it>0
            }
        }
    }
    fun setAlbumId(albumId:String){
        albumScope?.cancel()
        albumScope = viewModelScope.launch{
            appDatabaseRepository.countAlbumById(albumId).stateIn(viewModelScope).collectLatest {
                _albumIsSaved.value = it>0
            }
        }
    }

    fun setArtistId(artistId:String){
        artistScope?.cancel()
        artistScope = viewModelScope.launch{
            appDatabaseRepository.countArtistById(artistId).stateIn(viewModelScope).collectLatest {
                _artistIsSaved.value = it>0
            }
        }
    }
    fun setPlaylistId(playlistId:String){
        playlistScope?.cancel()
        playlistScope = viewModelScope.launch{
            appDatabaseRepository.countPlaylistById(playlistId).stateIn(viewModelScope).collectLatest {
                _playlistIsSaved.value = it>0
            }
        }
    }

    fun noSort(){
        tracks = appDatabaseRepository.getTracks().stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf()
        )
        albums = appDatabaseRepository.getAlbums().stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf()
        )
        artists = appDatabaseRepository.getArtists().stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf()
        )
        playlists = appDatabaseRepository.getPlaylists().stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf()
        )
    }

    fun sortByName(asc:Boolean){
        tracks = appDatabaseRepository.getTracksOrderByName(asc).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf())
        albums = appDatabaseRepository.getAlbumsOrderByName(asc).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf())
        artists = appDatabaseRepository.getArtistOrderByName(asc).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf())
        playlists = appDatabaseRepository.getPlaylistsOrderByName(asc).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf())
    }
    fun sortByDate(asc:Boolean){
        tracks = appDatabaseRepository.getTracksOrderByDate(asc).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf())
        albums = appDatabaseRepository.getAlbumsOrderByDate(asc).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf())
        artists = appDatabaseRepository.getArtistOrderByDate(asc).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf())
        playlists = appDatabaseRepository.getPlaylistsOrderByDate(asc).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            listOf())

    }


    suspend fun insertFavouriteTrack(track: Track){
        appDatabaseRepository.insertTrack(track)
    }
    suspend fun insertSavedAlbum(album: Album){
        appDatabaseRepository.insertAlbum(album)
    }
    suspend fun insertSavedArtist(artist: Artist){
        appDatabaseRepository.insertArtist(artist)
    }
    suspend fun insertSavedPlaylist(playlist: Playlist){
        appDatabaseRepository.insertPlaylist(playlist)
    }


    suspend fun deleteFavouriteTrack(track: Track){
        appDatabaseRepository.deleteTrackById(track.id)
    }
    suspend fun deleteSavedAlbum(album: Album){
        appDatabaseRepository.deleteAlbumById(album.id)
    }
    suspend fun deleteSavedArtist(artist: Artist){
        appDatabaseRepository.deleteArtist(artist.id)
    }
    suspend fun deleteSavedPlaylist(playlist: Playlist){
        appDatabaseRepository.deletePlaylistById(playlist.id)
    }

}