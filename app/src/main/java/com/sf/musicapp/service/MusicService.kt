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
import androidx.annotation.OptIn
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.NotificationUtil
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ControllerInfo
import androidx.media3.session.MediaSessionService
import androidx.media3.session.legacy.MediaSessionCompat
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

    private var mediaSession: MediaSession?=null


    override fun onGetSession(controllerInfo: ControllerInfo):MediaSession? {
        return mediaSession
    }

    override fun onCreate() {
        super.onCreate()

        player.prepare()
        mediaSession = MediaSession.Builder(this,player)
            .setId("MusicService")
            .setSessionActivity(createSessionActivityIntentPending())
            .build()
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this,"MEDIA_PLAYER")
            .setContentTitle("Playing")
            .build()
        startForeground(1,notification)
    }

    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            "MEDIA_PLAYER",
            "Phat Nhac",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createSessionActivityIntentPending(): PendingIntent{
        val sessionIntent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this,0,sessionIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        player.pause()
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        clearListener()
        mediaSession?.apply{
            release()
            mediaSession = null
            player.pause()
        }
        player.release()
        super.onDestroy()
    }

}


