package com.sf.musicapp.module

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import com.sf.musicapp.data.database.AppDatabase
import com.sf.musicapp.data.repository.DatabaseRepository
import com.sf.musicapp.data.repository.DatabaseRepositoryImpl
import com.sf.musicapp.network.repository.AlbumRepository
import com.sf.musicapp.network.repository.ArtistRepository
import com.sf.musicapp.network.repository.PlaylistRepository
import com.sf.musicapp.network.repository.TrackRepository
import com.sf.musicapp.network.repository.implement.AlbumRepositoryImpl
import com.sf.musicapp.network.repository.implement.ArtistRepositoryImpl
import com.sf.musicapp.network.repository.implement.PlaylistRepositoryImpl
import com.sf.musicapp.network.repository.implement.TrackRepositoryImpl
import com.sf.musicapp.network.retrofit.RetrofitInstance
import com.sf.musicapp.utils.PlayerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun getArtistRepository(): ArtistRepository
        = ArtistRepositoryImpl(RetrofitInstance.artistApi)

    @Provides
    @Singleton
    fun getPlayer(@ApplicationContext context: Context) : ExoPlayer =
        ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .setAudioAttributes(AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .build(),true)
            .build()


    @Provides
    @Singleton
    fun getTrackRepository(): TrackRepository
        = TrackRepositoryImpl(RetrofitInstance.trackApi)

    @Provides
    @Singleton
    fun getAlbumRepository(): AlbumRepository
        = AlbumRepositoryImpl(RetrofitInstance.albumApi)

    @Provides
    @Singleton
    fun getPlaylistRepository(): PlaylistRepository
        = PlaylistRepositoryImpl(RetrofitInstance.playlistApi)

    @Provides
    @Singleton
    fun getPlayerHelper(player: ExoPlayer): PlayerHelper
        = PlayerHelper(player)

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): AppDatabase
        = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun getDatabaseRepository(appDatabase: AppDatabase): DatabaseRepository
        = DatabaseRepositoryImpl(
            appDatabase.trackDao,
            appDatabase.albumDao,
            appDatabase.artistDao,
            appDatabase.playlistDao
        )
}