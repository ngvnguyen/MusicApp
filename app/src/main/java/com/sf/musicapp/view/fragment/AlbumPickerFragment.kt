package com.sf.musicapp.view.fragment

import android.transition.Visibility
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.ui.PlayerNotificationManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.adapter.paging.SmallItemAdapter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.FragmentAlbumPickerBinding
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.shareText
import com.sf.musicapp.view.activity.MainActivity
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumPickerFragment: BaseBottomSheetFragment<FragmentAlbumPickerBinding>() {
    @Inject
    lateinit var playerHelper: PlayerHelper

    private lateinit var album: Album
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var adapter: ItemAdapter
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment
    private var tracks = listOf<Track>()
    private lateinit var adapterPager: SmallItemAdapter

    override fun getViewBinding(): FragmentAlbumPickerBinding {
        return FragmentAlbumPickerBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()

        playMusicBottomFragment = (requireActivity() as MainActivity).playMusicBottomFragment

        binding.image.loadImg(album.image,R.drawable.musical_notes)
        binding.albumName.text = album.name

        adapter = ItemAdapter{ track->
            playerHelper.playNewTrack(track)
            playMusicBottomFragment.show(parentFragmentManager, playMusicBottomFragment.tag)
        }
        adapterPager = SmallItemAdapter{
            playerHelper.playNewTrack(it)
            playMusicBottomFragment.show(parentFragmentManager, playMusicBottomFragment.tag)
        }
        binding.albumRecyclerView.adapter = adapter
        binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launch{
            tracks = appViewModel.getAllTrackByAlbumId(album.id)

            if (tracks.isEmpty()){
                binding.noTrack.visibility = View.VISIBLE
            }else binding.noTrack.visibility = View.GONE

            if (tracks.size> Limits.MAX_ITEM) {
                adapter.setData(tracks.take(Limits.MAX_ITEM))
                binding.viewAll.visibility = View.VISIBLE
            }
            else {
                adapter.setData(tracks)
                binding.viewAll.visibility = View.GONE
            }


        }


    }

    override fun addEvent() {
        binding.playButton.setOnClickListener{
            lifecycleScope.launch{
                playerHelper.insertPlaylist(tracks)
                playMusicBottomFragment.show(parentFragmentManager, playMusicBottomFragment.tag)
                playerHelper.play()
            }
        }
        binding.viewAll.setOnClickListener{
            binding.albumRecyclerView.adapter = adapterPager
            lifecycleScope.launch{
                appViewModel.trackSetPager.collectLatest {
                    adapterPager.submitData(it)
                }
            }
            binding.viewAll.visibility = View.GONE
        }
        binding.shareButton.setOnClickListener{
            context?.shareText(album.shareUrl)
        }
    }

    override fun onStart() {
        super.onStart()


    }

    override fun initViewModel() {
        super.initViewModel()

    }

    @Deprecated(
        "Use show(manager, tag, album) instead",
        ReplaceWith("show(manager, tag, album)"),
        DeprecationLevel.ERROR
    )
    override fun show(manager: FragmentManager, tag: String?) {
        //super.show(manager, tag)
    }

    // dùng funtion thay cho hàm mặc định để set album
    fun show(manager: FragmentManager, tag: String?,album : Album){
        this.album= album
        super.show(manager,tag)
    }
}