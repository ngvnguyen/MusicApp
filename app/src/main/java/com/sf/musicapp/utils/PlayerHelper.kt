package com.sf.musicapp.utils

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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

    companion object{
        private const val PROGRESS_COUNT = 50L
    }
    enum class RepeatMode{
        OFF,
        ONE,
        ALL,
        SHUFFLE
    }

    //đơn vị ms
    private val _duration = MutableStateFlow(0L)
    val duration = _duration as StateFlow<Long>

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition = _currentPosition as StateFlow<Long>

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying as StateFlow<Boolean>

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack = _currentTrack as StateFlow<Track?>

    private var job: Job? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val playlist = mutableListOf<Track>()

    private val _isEnded = MutableStateFlow<Boolean>(false)
    val isEnded = _isEnded as StateFlow<Boolean>

    private val _currentRepeatMode = MutableStateFlow(RepeatMode.OFF)
    val currentRepeatMode = _currentRepeatMode as StateFlow<RepeatMode>

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
                        _isEnded.value = false
                    }

                    Player.STATE_ENDED -> if (canSeekToNextTrack()) {
                        job?.cancel()
                        player.seekToNext()
                        play()
                    } else {
                        stop()
                        _isEnded.value = true
                    }

                    Player.STATE_IDLE -> {

                    }
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
                delay(PROGRESS_COUNT)
                _currentPosition.value += PROGRESS_COUNT
            }

        }
    }


    fun insert(track: Track) {
        playlist.add(track)
        val mi = createMediaItem(track)
        player.addMediaItem(mi)
    }
    fun findTrackIndex(track:Track):Int?{
        val i = playlist.indexOf(track)
        return if (i==-1) null else i
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

    fun insertPlaylist(tracks: List<Track>) {
        clearMediaItem()
        val mis = mutableListOf<MediaItem>()
        playlist.addAll(tracks)
        playlist.forEach {
            val mi = createMediaItem(it)
            mis.add(mi)
        }
        player.addMediaItems(mis)
    }

    private fun createMediaItem(track: Track): MediaItem{
        return MediaItem.Builder()
            .setUri(track.audioDownload)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(track.name)
                    .setArtist(track.artistName)
                    .setArtworkUri(Uri.parse(track.image))
                    .build()
            )
            .build()
    }

    fun seekToMs(positionMs: Long) = player.seekTo(positionMs)
    fun seekTo(index:Int) = player.seekTo(index,0)

    fun prepare() = player.prepare()
    fun resume() {
        player.play()
    }

    fun play() {
        prepare()
        player.play()
    }

    fun replay(){
        player.seekTo(0,0)
        prepare()
        player.play()
    }

    fun stop() {
        player.stop()
    }

    fun pause() {
        player.pause()
    }

    fun updateRepeatMode(context: Context){
        val repeatMode = SharePreferences.getRepeatMode(context)
        when(repeatMode){
            RepeatMode.OFF -> {
                _currentRepeatMode.value = RepeatMode.OFF
                player.repeatMode = Player.REPEAT_MODE_OFF
                player.shuffleModeEnabled = false
            }

            RepeatMode.ONE -> {
                _currentRepeatMode.value = RepeatMode.ONE
                player.repeatMode = Player.REPEAT_MODE_ONE
                player.shuffleModeEnabled = false
            }
            RepeatMode.ALL -> {
                _currentRepeatMode.value = RepeatMode.ALL
                player.repeatMode = Player.REPEAT_MODE_ALL
                player.shuffleModeEnabled = false
            }
            RepeatMode.SHUFFLE -> {
                _currentRepeatMode.value = RepeatMode.SHUFFLE
                player.repeatMode = Player.REPEAT_MODE_OFF
                player.shuffleModeEnabled = true
            }
        }
    }

    fun changeRepeatMode(context: Context){
        when(_currentRepeatMode.value){
            RepeatMode.OFF -> {
                _currentRepeatMode.value = RepeatMode.ONE
                player.repeatMode = Player.REPEAT_MODE_ONE
                SharePreferences.setRepeatModePref(context,_currentRepeatMode.value)
            }
            RepeatMode.ONE -> {
                _currentRepeatMode.value = RepeatMode.ALL
                SharePreferences.setRepeatModePref(context,_currentRepeatMode.value)
                player.repeatMode = Player.REPEAT_MODE_ALL
            }
            RepeatMode.ALL -> {
                _currentRepeatMode.value = RepeatMode.SHUFFLE
                SharePreferences.setRepeatModePref(context,_currentRepeatMode.value)
                player.shuffleModeEnabled = true
            }
            RepeatMode.SHUFFLE -> {
                _currentRepeatMode.value = RepeatMode.OFF
                player.shuffleModeEnabled = false
                SharePreferences.setRepeatModePref(context,_currentRepeatMode.value)
                player.repeatMode = Player.REPEAT_MODE_OFF
            }
        }
    }

    fun seekToNextTrack() = player.seekToNextMediaItem()
    fun seekToPreviousTrack() = player.seekToPreviousMediaItem()

    fun canSeekToNextTrack() = player.hasNextMediaItem()
    fun canSeekToPreviousTrack() = player.hasPreviousMediaItem()
}