package com.sf.musicapp.module

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
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
    fun getPlayer(@ApplicationContext context: Context) : ExoPlayer
        = ExoPlayer.Builder(context).build()

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
}