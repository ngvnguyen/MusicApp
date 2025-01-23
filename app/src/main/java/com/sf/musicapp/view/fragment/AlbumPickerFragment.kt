package com.sf.musicapp.view.fragment

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.SmallItemAdapter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.databinding.FragmentAlbumPickerBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.view.activity.MainActivity
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import com.sf.musicapp.view.fragment.viewmodel.AlbumPickerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumPickerFragment: BaseBottomSheetFragment<FragmentAlbumPickerBinding>() {
    @Inject
    lateinit var playerHelper: PlayerHelper

    private lateinit var album: Album
    private val albumPickerViewModel: AlbumPickerViewModel by activityViewModels()
    private lateinit var adapter: SmallItemAdapter
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment

    override fun getViewBinding(): FragmentAlbumPickerBinding {
        return FragmentAlbumPickerBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()

        playMusicBottomFragment = (requireActivity() as MainActivity).playMusicBottomFragment

        binding.image.loadImg(album.image,R.drawable.musical_notes)

        adapter = SmallItemAdapter{ track->
            playerHelper.playNewTrack(track)
            playMusicBottomFragment.show(parentFragmentManager, "play music",
                PlayMusicBottomFragment.NEW_PLAY)
        }
        binding.albumRecyclerView.adapter = adapter
        binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        albumPickerViewModel.setAlbumId(album.id)

        lifecycleScope.launch{
            albumPickerViewModel.trackPager.collectLatest {
                adapter.submitData(it)
            }
        }

        binding.playButton.setOnClickListener{
            lifecycleScope.launch{
                playerHelper.clearMediaItem()
                val tracks = albumPickerViewModel.getAllTrack()
                playerHelper.insertNewPlaylist(tracks)
                playMusicBottomFragment.show(parentFragmentManager, "play music",
                    PlayMusicBottomFragment.NEW_PLAY)
                playerHelper.play()
            }
        }


    }

    override fun onStart() {
        super.onStart()


    }

    override fun initViewModel() {
        super.initViewModel()

    }

    @Deprecated(
        "Use show(manager, tag, track) instead",
        ReplaceWith("show(manager, tag, album)"),
        DeprecationLevel.ERROR
    )
    override fun show(manager: FragmentManager, tag: String?) {
        //super.show(manager, tag)
    }
    fun show(manager: FragmentManager, tag: String?,album : Album){
        this.album= album
        super.show(manager,tag)
    }
}