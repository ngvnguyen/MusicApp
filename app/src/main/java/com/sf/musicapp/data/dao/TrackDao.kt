package com.sf.musicapp.data.dao



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sf.musicapp.data.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Query("SELECT * FROM track")
    fun getTracks(): Flow<List<Track>>

    @Query("""
        SELECT * FROM track
        ORDER BY 
            CASE WHEN :sort ='ASC' THEN name END ASC,
            CASE WHEN :sort ='DESC' THEN name END DESC
    """)
    fun getTracksOrderByName(sort:String): Flow<List<Track>>

    @Query("""
        SELECT * FROM track
        ORDER BY 
            CASE WHEN :sort ='ASC' THEN releaseDate END ASC,
            CASE WHEN :sort ='DESC' THEN releaseDate END DESC
    """)
    fun getTracksOrderByDate(sort: String):Flow<List<Track>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: Track)

    @Query("""
        DELETE FROM track
        WHERE id=:id
    """)
    suspend fun deleteTrackById(id:String)

    @Update
    suspend fun updateTrack(track: Track)

    @Query("""
        SELECT * FROM track WHERE name LIKE '%'||:query||'%'
        ORDER BY name ASC
    """)
    fun searchTrackByName(query:String):Flow<List<Track>>

    @Query("""
        SELECT COUNT(*) FROM track WHERE id=:id
    """)
    fun countTrackById(id:String):Flow<Int>

    @Query("""
        SELECT EXISTS(SELECT 1 FROM track WHERE id=:id)
    """)
    fun checkTrackExists(id:String):Flow<Boolean>
}