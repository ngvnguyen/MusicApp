package com.sf.musicapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sf.musicapp.data.model.Playlist
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    fun getPlaylists(): Flow<List<Playlist>>

    @Query("""
        SELECT * FROM playlist
        ORDER BY 
            CASE WHEN :sort ='ASC' THEN name END ASC,
            CASE WHEN :sort ='DESC' THEN name END DESC
    """)
    fun getPlaylistOrderByName(sort:String): Flow<List<Playlist>>

    @Query("""
        SELECT * FROM playlist
        ORDER BY 
            CASE WHEN :sort ='ASC' THEN creationDate END ASC,
            CASE WHEN :sort ='DESC' THEN creationDate END DESC
    """)
    fun getPlaylistOrderByDate(sort: String):Flow<List<Playlist>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: Playlist)

    @Query("""
        DELETE FROM playlist
        WHERE id=:id
    """)
    suspend fun deletePlaylistById(id:String)

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Query("""
        SELECT * FROM playlist WHERE name LIKE '%'||:query||'%'
        ORDER BY name ASC
    """)
    fun searchPlaylistByName(query:String):Flow<List<Playlist>>

    @Query("""
        SELECT COUNT(*) FROM playlist WHERE id=:id
    """)
    fun countPlaylistById(id:String):Flow<Int>
}