package com.sf.musicapp.utils

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.sf.musicapp.data.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerHelper(
    val player:ExoPlayer
) {
    //ms
    private val _duration = MutableStateFlow(0L)
    val duration = _duration as StateFlow<Long>

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition = _currentPosition as StateFlow<Long>

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying as StateFlow<Boolean>

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack = _currentTrack as StateFlow<Track?>

    private var job: Job? = null
    private val progressCount = 50L
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val playlist = mutableListOf<Track>()

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_BUFFERING -> job?.cancel()
                    Player.STATE_READY -> {
                        _currentPosition.value = player.currentPosition
                        _duration.value = player.duration
                        if (_isPlaying.value) updateJob()
                    }

                    Player.STATE_ENDED -> if (canSeekToNextTrack()) {
                        job?.cancel()
                        player.seekToNext()
                        play()
                    } else stop()
                }


            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                _currentPosition.value = player.currentPosition
                _duration.value = player.duration
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                _isPlaying.value = isPlaying
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                _currentTrack.value = playlist.getOrNull(player.currentMediaItemIndex)
            }
        })

        scope.launch {
            isPlaying.collectLatest { isPlaying ->
                if (isPlaying) updateJob()
                else job?.cancel()
            }
        }
    }

    private fun updateJob() {
        job?.cancel()
        job = scope.launch {
            while (isActive) {
                delay(progressCount)
                _currentPosition.value += progressCount
            }

        }
    }


    fun insert(track: Track) {
        playlist.add(track)
        val mi = MediaItem.fromUri(track.audioDownload)
        player.addMediaItem(mi)
    }

    fun playNewTrack(track: Track) {
        clearMediaItem()
        player.clearMediaItems()
        insert(track)
        play()
    }

    fun clearMediaItem() {
        playlist.clear()
        player.stop()
        player.clearMediaItems()
    }

    fun insertNewPlaylist(tracks: List<Track>) {
        val mis = mutableListOf<MediaItem>()
        playlist.addAll(tracks)
        playlist.forEach {
            val mi = MediaItem.fromUri(it.audioDownload)
            mis.add(mi)
        }
        player.addMediaItems(mis)
        prepare()
    }

    fun seekTo(positionMs: Long) = player.seekTo(positionMs)

    fun prepare() = player.prepare()
    fun resume() {
        player.play()
        updateJob()
    }

    fun play() {
        prepare()
        player.play()
    }

    fun stop() {
        player.stop()
        job?.cancel()
    }

    fun pause() {
        player.pause()
        job?.cancel()
    }
    fun seekToNextTrack() = player.seekToNextMediaItem()
    fun seekToPreviousTrack() = player.seekToPreviousMediaItem()

    fun canSeekToNextTrack() = player.hasNextMediaItem()
    fun canSeekToPreviousTrack() = player.hasPreviousMediaItem()
}