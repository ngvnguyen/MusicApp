package com.sf.musicapp.data.repository

import com.sf.musicapp.data.dao.TrackDao
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.data.model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getTracks(): Flow<List<Track>>
    fun getTracksOrderByName(asc:Boolean): Flow<List<Track>>
    fun getTracksOrderByDate(asc:Boolean): Flow<List<Track>>
    fun countTrackById(id: String): Flow<Int>
    fun checkTrackExists(id: String): Flow<Boolean>

    suspend fun insertTrack(track: Track)
    suspend fun deleteTrackById(id:String)


    fun getAlbums(): Flow<List<Album>>
    fun getAlbumsOrderByName(asc:Boolean): Flow<List<Album>>
    fun getAlbumsOrderByDate(asc:Boolean): Flow<List<Album>>
    fun countAlbumById(id: String): Flow<Int>
    fun checkAlbumExists(id: String): Flow<Boolean>

    suspend fun insertAlbum(album: Album)
    suspend fun deleteAlbumById(id:String)


    fun getArtists(): Flow<List<Artist>>
    fun getArtistOrderByName(asc:Boolean): Flow<List<Artist>>
    fun getArtistOrderByDate(asc:Boolean): Flow<List<Artist>>
    fun countArtistById(id: String): Flow<Int>
    fun checkArtistExists(id: String): Flow<Boolean>

    suspend fun insertArtist(artist:Artist)
    suspend fun deleteArtist(id:String)


    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistsOrderByName(asc:Boolean): Flow<List<Playlist>>
    fun getPlaylistsOrderByDate(asc:Boolean): Flow<List<Playlist>>
    fun countPlaylistById(id: String): Flow<Int>
    fun checkPlaylistExists(id: String): Flow<Boolean>

    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylistById(id:String)
}