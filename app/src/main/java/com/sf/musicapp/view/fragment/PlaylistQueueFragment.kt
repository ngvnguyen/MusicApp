package com.sf.musicapp.view.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.FragmentPlaylistQueueBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistQueueFragment : BaseBottomSheetFragment<FragmentPlaylistQueueBinding>() {

    @Inject
    lateinit var playerHelper: PlayerHelper
    private lateinit var adapter: ItemAdapter<Track>


    override fun getViewBinding(): FragmentPlaylistQueueBinding {
        return FragmentPlaylistQueueBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        adapter = object : ItemAdapter<Track>(
            itemClick = {track->
                playerHelper.findTrackIndex(track)?.let { playerHelper.seekTo(it) }
                dismiss()
            }){
            override fun bind(
                binding: ItemSmallLayoutBinding,
                data: Track
            ) {
                binding.itemAuthor.text = data.artistName
                binding.itemTitle.text = data.name
                binding.itemImg.loadImg(data.image, R.drawable.server)
            } }

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