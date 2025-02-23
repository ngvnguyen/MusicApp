package com.sf.musicapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sf.musicapp.data.model.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM album")
    fun getAlbums(): Flow<List<Album>>

    @Query("""
        SELECT * FROM album
        ORDER BY 
            CASE WHEN :sort ='ASC' THEN name END ASC,
            CASE WHEN :sort ='DESC' THEN name END DESC
    """)
    fun getAlbumsOrderByName(sort:String): Flow<List<Album>>

    @Query("""
        SELECT * FROM album
        ORDER BY 
            CASE WHEN :sort ='ASC' THEN releaseDate END ASC,
            CASE WHEN :sort ='DESC' THEN releaseDate END DESC
    """)
    fun getAlbumsOrderByDate(sort: String):Flow<List<Album>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(album: Album)

    @Query("""
        DELETE FROM album
        WHERE id=:id
    """)
    suspend fun deleteAlbumById(id:String)

    @Update
    suspend fun updateAlbum(album: Album)

    @Query("""
        SELECT * FROM album WHERE name LIKE '%'||:query||'%'
        ORDER BY name ASC
    """)
    fun searchAlbumByName(query:String):Flow<List<Album>>

    @Query("""
        SELECT COUNT(*) FROM album WHERE id=:id
    """)
    fun countAlbumById(id:String):Flow<Int>
}