package com.sf.musicapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.NotificationUtil
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ControllerInfo
import androidx.media3.session.MediaSessionService
import com.sf.musicapp.network.repository.TrackRepository
import com.sf.musicapp.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MusicService: MediaSessionService() {
    @Inject
    lateinit var player: ExoPlayer
    @Inject
    lateinit var trackRepository: TrackRepository
    private var currentPage = 0

    private lateinit var mediaSession: MediaSession


    override fun onGetSession(controllerInfo: ControllerInfo):MediaSession? {
        return mediaSession
    }

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this,player)
            .setId("MusicService")
            .setSessionActivity(createSessionActivityIntentPending())
            .build()


    }

    private fun createSessionActivityIntentPending(): PendingIntent{
        val sessionIntent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this,0,sessionIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun loadInitialMediaItem(page:Int){
        CoroutineScope(Dispatchers.Main).launch{

        }
    }

    override fun onDestroy() {
        player.release()
        mediaSession.release()
        super.onDestroy()
    }


}

