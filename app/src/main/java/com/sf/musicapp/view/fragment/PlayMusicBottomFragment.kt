package com.sf.musicapp.view.fragment

import android.content.DialogInterface
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sf.musicapp.databinding.FragmentPlayMusicBinding
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import com.sf.musicapp.R
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.isNetworkAvailable
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.toDuration
import com.sf.musicapp.view.activity.viewmodel.DBViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayMusicBottomFragment(
    private val onClose:()->Unit={}
): BaseBottomSheetFragment<FragmentPlayMusicBinding>() {

    private val dbViewModel: DBViewModel by activityViewModels()

    @Inject
    lateinit var playerHelper: PlayerHelper




    override fun getViewBinding(): FragmentPlayMusicBinding {
        return FragmentPlayMusicBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()
        playerHelper.updateRepeatMode(requireActivity())

        lifecycleScope.launch{

            // cập nhật thanh tiến trình
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
        if (playerHelper.isPlaying.value){
            binding.playButton.setIconResource(R.drawable.play)
        }else binding.playButton.setIconResource(R.drawable.pause)

        binding.seekbar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                p0: SeekBar?,
                p1: Int,
                p2: Boolean
            ) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.let { playerHelper.seekToMs(it.progress.toLong())}
            }
        })

        if (requireContext().isNetworkAvailable())
            Toast.makeText(requireContext(),"Network unavailable", Toast.LENGTH_SHORT).show()

    }

    override fun addEvent() {
        super.addEvent()
        binding.playButton.setOnClickListener{
            if (playerHelper.isEnded.value){
                playerHelper.replay()
            }
            else if (playerHelper.isPlaying.value){
                playerHelper.pause()
            }else{
                playerHelper.resume()
            }
        }

        binding.favouriteButton.setOnClickListener{
            lifecycleScope.launch{
                if (dbViewModel.trackIsFavourite.value){
                    playerHelper.currentTrack.value?.let { dbViewModel.deleteFavouriteTrack(it) }
                }else{
                    playerHelper.currentTrack.value?.let { dbViewModel.insertFavouriteTrack(it) }
                }
            }

        }

        binding.forwardButton.setOnClickListener{
            if (playerHelper.canSeekToNextTrack()) playerHelper.seekToNextTrack()
        }
        binding.previousButton.setOnClickListener{
            if (playerHelper.canSeekToPreviousTrack()) playerHelper.seekToPreviousTrack()
        }
        binding.repeatModeButton.setOnClickListener{
            playerHelper.changeRepeatMode(requireActivity())
        }

        val playlistQueueFragment = PlaylistQueueFragment()
        binding.playlist.setOnClickListener{
            playlistQueueFragment.show(parentFragmentManager,playlistQueueFragment.tag)
        }
    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{

            // theo dõi trạng thái của player
            launch{
                playerHelper.isPlaying.collectLatest {isPlaying->
                    if (isPlaying){
                        binding.playButton.setIconResource(R.drawable.pause)
                    }else{
                        if(playerHelper.isEnded.value.not())
                            binding.playButton.setIconResource(R.drawable.play)
                    }
                }
            }
            launch{
                playerHelper.currentTrack.collectLatest { track->
                    track?.let{
                        dbViewModel.setTrackId(track.id)
                        binding.artist.text = it.artistName
                        binding.title.text = it.name
                        binding.playerView.loadImg(it.image)
                    }
                }
            }
            launch{
                playerHelper.isEnded.collectLatest {
                    if (it){
                        binding.playButton.setIconResource(R.drawable.replay)
                    }
                }
            }
            launch{
                dbViewModel.trackIsFavourite.collectLatest {
                    binding.favouriteButton
                        .setIconResource(if (it) R.drawable.heart else R.drawable.heart_outline)
                }
            }

            launch{
                playerHelper.currentRepeatMode.collectLatest {
                    when(it){
                        PlayerHelper.RepeatMode.OFF -> binding.repeatModeButton.setIconResource(R.drawable.close)
                        PlayerHelper.RepeatMode.ONE -> binding.repeatModeButton.setIconResource(R.drawable.repeate_one)
                        PlayerHelper.RepeatMode.ALL -> binding.repeatModeButton.setIconResource(R.drawable.repeat_all)
                        PlayerHelper.RepeatMode.SHUFFLE -> binding.repeatModeButton.setIconResource(R.drawable.shuffle)
                    }
                }
            }
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