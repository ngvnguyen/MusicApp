package com.sf.musicapp.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.dao.AlbumDao
import com.sf.musicapp.data.dao.ArtistDao
import com.sf.musicapp.data.dao.PlaylistDao
import com.sf.musicapp.data.dao.TrackDao
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.data.model.Track

@TypeConverters(DateConverter::class)
@Database(entities = [
    Track::class,
    Album::class,
    Artist::class,
    Playlist::class],version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val trackDao: TrackDao
    abstract val albumDao: AlbumDao
    abstract val artistDao: ArtistDao
    abstract val playlistDao: PlaylistDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = synchronized(this){
            instance?: Room.databaseBuilder(context, AppDatabase::class.java,"app_database")
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }

        }
    }

}