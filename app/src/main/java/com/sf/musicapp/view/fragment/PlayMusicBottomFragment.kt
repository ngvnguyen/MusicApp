package com.sf.musicapp.view.fragment

import android.app.ActionBar
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.annotation.OptIn
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sf.musicapp.databinding.FragmentPlayMusicBinding
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import com.sf.musicapp.R
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.service.MusicService
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.text.toInt

@AndroidEntryPoint
class PlayMusicBottomFragment(
    private val onClose:(Track)->Unit={}
): BaseBottomSheetFragment<FragmentPlayMusicBinding>() {

    companion object{
        const val NEW_PLAY =1
        const val ON_GOING =2
    }

    private var action:Int = NEW_PLAY
    //private val viewModel: AppViewModel by activityViewModels()
    @Inject
    lateinit var player: ExoPlayer
    private lateinit var track: Track
    private var updateJob: Job?=null
    private lateinit var listener:Player.Listener


    override fun getViewBinding(): FragmentPlayMusicBinding {
        return FragmentPlayMusicBinding.inflate(layoutInflater)
    }
    @Deprecated("Để test thôi")
    private var isFavourite = false

    override fun initView() {
        super.initView()
        val playerView = binding.playerView
        playerView.player = player

        if (action == NEW_PLAY){
            val mediaItem = MediaItem.fromUri(track.audioDownload)
            player.clearMediaItems()
            player.addMediaItem(mediaItem)
            player.play()
        }
        binding.artist.text = track.artistName
        binding.title.text = track.name
        if (action == ON_GOING){
            binding.seekbar.max = player.duration.toInt()
            binding.seekbar.progress = player.currentPosition.toInt()
            updateSeekbar()
        }

        binding.seekbar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                p0: SeekBar?,
                p1: Int,
                p2: Boolean
            ) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.let { player.seekTo(it.progress.toLong()) }
            }
        })

        listener = object: Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_BUFFERING){
                    updateJob?.cancel()
                }
                if (playbackState == Player.STATE_READY){
                    binding.seekbar.max = player.duration.toInt()
                    binding.seekbar.progress = player.currentPosition.toInt()
                    updateSeekbar()
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                binding.seekbar.progress = newPosition.positionMs.toInt()
                binding.seekbar.max = player.duration.toInt()
            }
        }

        player.addListener(listener)


    }

    private fun updateSeekbar(){
        updateJob?.cancel()
        updateJob = CoroutineScope(Dispatchers.Main).launch{
            val progressCount = 50
            while(true){
                binding.seekbar.progress+=progressCount
                delay(progressCount.toLong())
            }
        }

    }

    override fun addEvent() {
        super.addEvent()
        binding.playButton.setOnClickListener{
            if (player.isPlaying){
                player.pause()
                binding.playButton.setIconResource(R.drawable.play)
            }else{
                player.play()
                binding.playButton.setIconResource(R.drawable.pause)
            }
        }

        binding.favouriteButton.setOnClickListener{
            onFavouriteButtonClicked()
        }

    }

    @Deprecated(
        "Use show(manager, tag, track) instead",
        ReplaceWith("show(manager, tag, track, action)"),
        DeprecationLevel.ERROR
    )
    override fun show(manager: FragmentManager, tag: String?) {
        //super.show(manager, tag)
    }

    fun show(manager: FragmentManager, tag: String?,track:Track?,action:Int){
        super.show(manager,tag)
        if (track!=null) this.track = track
        this.action = action
    }

    private fun onFavouriteButtonClicked(){
        isFavourite = !isFavourite
        if (isFavourite){
            binding.favouriteButton.setIconResource(R.drawable.heart)
        }else{
            binding.favouriteButton.setIconResource(R.drawable.heart_outline)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ActionBar.LayoutParams.MATCH_PARENT
            bottomSheet.background = null
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN){
                        dismiss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

            })
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onClose(track)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onClose(track)
    }

    override fun onDestroy() {
        super.onDestroy()
        updateJob?.cancel()
        player.removeListener(listener)
    }
}