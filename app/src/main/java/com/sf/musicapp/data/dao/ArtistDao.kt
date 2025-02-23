package com.sf.musicapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sf.musicapp.data.model.Artist
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artist")
    fun getArtists(): Flow<List<Artist>>

    @Query("""
        SELECT * FROM artist
        ORDER BY 
            CASE WHEN :sort ='ASC' THEN name END ASC,
            CASE WHEN :sort ='DESC' THEN name END DESC
    """)
    fun getArtistsOrderByName(sort:String): Flow<List<Artist>>

    @Query("""
        SELECT * FROM artist
        ORDER BY 
            CASE WHEN :sort ='ASC' THEN joinDate END ASC,
            CASE WHEN :sort ='DESC' THEN joinDate END DESC
    """)
    fun getArtistOrderByDate(sort: String):Flow<List<Artist>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtist(artist:Artist)

    @Query("""
        DELETE FROM artist
        WHERE id=:id
    """)
    suspend fun deleteArtistById(id:String)

    @Update
    suspend fun updateArtist(artist: Artist)

    @Query("""
        SELECT * FROM artist WHERE name LIKE '%'||:query||'%'
        ORDER BY name ASC
    """)
    fun searchArtistByName(query:String):Flow<List<Artist>>

    @Query("""
        SELECT COUNT(*) FROM artist WHERE id=:id
    """)
    fun countArtistById(id:String):Flow<Int>

    @Query("""
        SELECT EXISTS(SELECT 1 FROM artist WHERE id=:id)
    """)
    fun checkArtistExists(id:String):Flow<Boolean>
}