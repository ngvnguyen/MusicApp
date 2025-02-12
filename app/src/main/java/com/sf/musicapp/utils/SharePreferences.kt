package com.sf.musicapp.utils

import android.content.Context

object SharePreferences {
    const val PLAYER_SETTINGS = "player_settings"
    const val REPEAT_MODE ="repeat_mode"

    fun setRepeatModePref(context: Context,repeatMode: PlayerHelper.RepeatMode){
        val sharedPreferences = context.getSharedPreferences(PLAYER_SETTINGS, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(REPEAT_MODE,repeatMode.ordinal).apply()

    }

    fun getRepeatMode(context: Context):PlayerHelper.RepeatMode{
        val sharedPreferences = context.getSharedPreferences(PLAYER_SETTINGS, Context.MODE_PRIVATE)
        return PlayerHelper.RepeatMode.entries[sharedPreferences.getInt(REPEAT_MODE, PlayerHelper.RepeatMode.OFF.ordinal)]
    }
}