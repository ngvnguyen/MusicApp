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
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.toDuration
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.text.toInt

@AndroidEntryPoint
class PlayMusicBottomFragment(
    private val onClose:()->Unit={}
): BaseBottomSheetFragment<FragmentPlayMusicBinding>() {

    companion object{
        const val NEW_PLAY =1
        const val ON_GOING =2
    }

    private var action:Int = NEW_PLAY
    //private val viewModel: AppViewModel by activityViewModels()

    @Inject
    lateinit var playerHelper: PlayerHelper




    override fun getViewBinding(): FragmentPlayMusicBinding {
        return FragmentPlayMusicBinding.inflate(layoutInflater)
    }
    @Deprecated("Để test thôi")
    private var isFavourite = false

    override fun initView() {
        super.initView()
        val playerView = binding.playerView
        playerView.player = playerHelper.player


        if (action == NEW_PLAY){
            playerHelper.play()
        }

        lifecycleScope.launch{
            launch{
                playerHelper.duration.collectLatest { duration->
                    binding.seekbar.max = duration.toInt()
                }
            }
            launch{
                playerHelper.currentPosition.collectLatest { progress->
                    binding.seekbar.progress = progress.toInt()
                    binding.duration.text = (progress/1000).toDuration()
                }
            }

        }
        if (action == ON_GOING&& !playerHelper.isPlaying.value){
            binding.playButton.setIconResource(R.drawable.play)
        }

        binding.seekbar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                p0: SeekBar?,
                p1: Int,
                p2: Boolean
            ) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.let { playerHelper.seekTo(it.progress.toLong())}
            }
        })

    }

    override fun addEvent() {
        super.addEvent()
        binding.playButton.setOnClickListener{
            if (playerHelper.isPlaying.value){
                playerHelper.pause()
            }else{
                playerHelper.resume()
            }
        }

        binding.favouriteButton.setOnClickListener{
            onFavouriteButtonClicked()
        }

        binding.forwardButton.setOnClickListener{
            if (playerHelper.canSeekToNextTrack()) playerHelper.seekToNextTrack()
        }
        binding.previousButton.setOnClickListener{
            if (playerHelper.canSeekToPreviousTrack()) playerHelper.seekToPreviousTrack()
        }

    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            launch{
                playerHelper.isPlaying.collectLatest {isPlaying->
                    if (isPlaying){
                        binding.playButton.setIconResource(R.drawable.pause)
                    }else{
                        binding.playButton.setIconResource(R.drawable.play)
                    }
                }
            }
            launch{
                playerHelper.currentTrack.collectLatest { track->
                    track?.let{
                        binding.artist.text = it.artistName
                        binding.title.text = it.name
                    }
                }
            }
        }

    }

    @Deprecated(
        "Use show(manager, tag, track) instead",
        ReplaceWith("show(manager, tag, action)"),
        DeprecationLevel.ERROR
    )
    override fun show(manager: FragmentManager, tag: String?) {
        //super.show(manager, tag)
    }

    fun show(manager: FragmentManager, tag: String?,action:Int){
        super.show(manager,tag)
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



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onClose()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onClose()
    }
}