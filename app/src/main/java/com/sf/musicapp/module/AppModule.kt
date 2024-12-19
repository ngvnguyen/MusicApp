package com.sf.musicapp.module

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.sf.musicapp.network.repository.ArtistRepository
import com.sf.musicapp.network.repository.TrackRepository
import com.sf.musicapp.network.repository.implement.ArtistRepositoryImpl
import com.sf.musicapp.network.repository.implement.TrackRepositoryImpl
import com.sf.musicapp.network.retrofit.RetrofitInstance
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

}