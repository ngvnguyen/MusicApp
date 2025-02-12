package com.sf.musicapp.view.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.databinding.FragmentPlaylistQueueBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistQueueFragment : BaseBottomSheetFragment<FragmentPlaylistQueueBinding>() {

    @Inject
    lateinit var playerHelper: PlayerHelper
    private lateinit var adapter: ItemAdapter


    override fun getViewBinding(): FragmentPlaylistQueueBinding {
        return FragmentPlaylistQueueBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        adapter = ItemAdapter{track->
            playerHelper.findTrackIndex(track)?.let { playerHelper.seekTo(it) }
            dismiss()
        }
        binding.playlistRecyclerView.adapter= adapter
        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter.setData(playerHelper.playlist.toList())

    }

    override fun addEvent() {
        super.addEvent()
        binding.playlist.setOnClickListener{
            dismiss()
        }
    }

}