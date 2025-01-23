package com.sf.musicapp

import android.app.Application
import android.content.Intent
import com.sf.musicapp.service.MusicService
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MusicApp: Application(){
    override fun onCreate() {
        super.onCreate()
        val musicService = Intent(this, MusicService::class.java)
        startForegroundService(musicService)
    }
}