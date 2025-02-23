package com.sf.musicapp.data.repository

import com.sf.musicapp.data.dao.AlbumDao
import com.sf.musicapp.data.dao.ArtistDao
import com.sf.musicapp.data.dao.PlaylistDao
import com.sf.musicapp.data.dao.TrackDao
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.data.utils.Constraint
import kotlinx.coroutines.flow.Flow

class DatabaseRepositoryImpl(
    private val trackDao: TrackDao,
    private val albumDao: AlbumDao,
    private val artistDao: ArtistDao,
    private val playlistDao: PlaylistDao
): DatabaseRepository {
    override fun getTracks(): Flow<List<Track>> {
        return trackDao.getTracks()
    }

    override fun getTracksOrderByName(asc:Boolean): Flow<List<Track>> {
        return trackDao.getTracksOrderByName(if(asc) Constraint.ASC else Constraint.DESC )
    }

    override fun getTracksOrderByDate(asc:Boolean): Flow<List<Track>> {
        return trackDao.getTracksOrderByDate(if(asc) Constraint.ASC else Constraint.DESC )
    }

    override fun countTrackById(id: String): Flow<Int> {
        return trackDao.countTrackById(id)
    }

    override fun checkTrackExists(id: String): Flow<Boolean> {
        return trackDao.checkTrackExists(id)
    }

    override suspend fun insertTrack(track: Track) {
        trackDao.insertTrack(track)
    }

    override suspend fun deleteTrackById(id: String) {
        trackDao.deleteTrackById(id)
    }



    override fun getAlbums(): Flow<List<Album>> {
        return albumDao.getAlbums()
    }

    override fun getAlbumsOrderByName(asc: Boolean): Flow<List<Album>> {
        return albumDao.getAlbumsOrderByName(if(asc) Constraint.ASC else Constraint.DESC )
    }

    override fun getAlbumsOrderByDate(asc: Boolean): Flow<List<Album>> {
        return albumDao.getAlbumsOrderByDate(if(asc) Constraint.ASC else Constraint.DESC )
    }

    override fun countAlbumById(id: String): Flow<Int> {
        return albumDao.countAlbumById(id)
    }

    override fun checkAlbumExists(id: String): Flow<Boolean> {
        return albumDao.checkAlbumExists(id)
    }

    override suspend fun insertAlbum(album: Album) {
        return albumDao.insertAlbum(album)
    }

    override suspend fun deleteAlbumById(id: String) {
        return albumDao.deleteAlbumById(id)
    }




    override fun getArtists(): Flow<List<Artist>> {
        return artistDao.getArtists()
    }

    override fun getArtistOrderByName(asc: Boolean): Flow<List<Artist>> {
        return artistDao.getArtistsOrderByName(if(asc) Constraint.ASC else Constraint.DESC )
    }

    override fun getArtistOrderByDate(asc: Boolean): Flow<List<Artist>> {
        return artistDao.getArtistOrderByDate(if(asc) Constraint.ASC else Constraint.DESC )
    }

    override fun countArtistById(id: String): Flow<Int> {
        return artistDao.countArtistById(id)
    }

    override fun checkArtistExists(id: String): Flow<Boolean> {
        return artistDao.checkArtistExists(id)
    }

    override suspend fun insertArtist(artist: Artist) {
        return artistDao.insertArtist(artist)
    }

    override suspend fun deleteArtist(id: String) {
        return artistDao.deleteArtistById(id)
    }



    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists()
    }

    override fun getPlaylistsOrderByName(asc: Boolean): Flow<List<Playlist>> {
        return playlistDao.getPlaylistOrderByName(if(asc) Constraint.ASC else Constraint.DESC )
    }

    override fun getPlaylistsOrderByDate(asc: Boolean): Flow<List<Playlist>> {
        return playlistDao.getPlaylistOrderByDate(if(asc) Constraint.ASC else Constraint.DESC )
    }

    override fun countPlaylistById(id: String): Flow<Int> {
        return playlistDao.countPlaylistById(id)
    }

    override fun checkPlaylistExists(id: String): Flow<Boolean> {
        return playlistDao.checkPlaylistExists(id)
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        return playlistDao.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylistById(id: String) {
        return playlistDao.deletePlaylistById(id)
    }
}