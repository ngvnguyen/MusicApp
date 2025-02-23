package com.sf.musicapp.view.fragment

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.adapter.paging.TrackItemAdapter
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.FragmentPlaylistPickerBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.utils.Jamendo
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.shareText
import com.sf.musicapp.view.activity.MainActivity
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.activity.viewmodel.DBViewModel
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistPickerFragment : BaseBottomSheetFragment<FragmentPlaylistPickerBinding>() {

    @Inject
    lateinit var playerHelper: PlayerHelper

    private val dbViewModel: DBViewModel by activityViewModels()
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var adapter: ItemAdapter<Track>
    private lateinit var playlist: Playlist
    private var tracks = listOf<Track>()
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment
    private lateinit var adapterPager: TrackItemAdapter

    override fun getViewBinding(): FragmentPlaylistPickerBinding {
        return FragmentPlaylistPickerBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        playMusicBottomFragment = (requireActivity() as MainActivity).playMusicBottomFragment
        binding.playlistName.text = playlist.name

        adapter = object : ItemAdapter<Track>(
            itemClick = {
                playerHelper.playNewTrack(it)
                playMusicBottomFragment.show(parentFragmentManager, playMusicBottomFragment.tag)
            }){
            override fun bind(
                binding: ItemSmallLayoutBinding,
                data: Track
            ) {
                binding.itemAuthor.text = data.artistName
                binding.itemTitle.text = data.name
                binding.itemImg.loadImg(data.image, R.drawable.server)
            } }
        adapterPager = TrackItemAdapter{
            playerHelper.playNewTrack(it)
            playMusicBottomFragment.show(parentFragmentManager, playMusicBottomFragment.tag)
        }
        binding.playlistRecyclerView.adapter = adapter
        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launch{
            tracks = appViewModel.getAllTrackByPlaylistId(playlist.id)
            if (tracks.isEmpty()){
                binding.noTrack.visibility = View.VISIBLE
            }else binding.noTrack.visibility = View.GONE


            if (tracks.size>= Limits.MAX_ITEM) {
                adapter.setData(tracks.take(Limits.MAX_ITEM))
                binding.viewAll.visibility = View.VISIBLE
            }
            else {
                adapter.setData(tracks)
                binding.viewAll.visibility = View.GONE
            }
        }

        dbViewModel.setPlaylistId(playlist.id)
    }

    override fun addEvent() {
        super.addEvent()
        binding.playButton.setOnClickListener{
            playerHelper.insertPlaylist(tracks)
            playerHelper.play()
            playMusicBottomFragment.show(parentFragmentManager,playMusicBottomFragment.tag)
        }
        binding.shareButton.setOnClickListener{
            context?.shareText(Jamendo.getSharePlaylistUrl(playlist.id))
        }
        binding.viewAll.setOnClickListener{
            binding.playlistRecyclerView.adapter = adapterPager
            appViewModel.setTracksPager(tracks)
            lifecycleScope.launch{
                appViewModel.trackSetPager.collectLatest {
                    adapterPager.submitData(it)
                }
            }
            binding.viewAll.visibility = View.GONE
        }

        binding.saveButton.setOnClickListener{
            lifecycleScope.launch{
                if (dbViewModel.playlistIsSaved.value){
                    dbViewModel.deleteSavedPlaylist(playlist)
                }else dbViewModel.insertSavedPlaylist(playlist)
            }
        }
    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            dbViewModel.playlistIsSaved.collectLatest {
                binding.saveButton.setIconTintResource(if (it) R.color.blue else R.color.white)
            }

        }
    }


    @Deprecated("Use show(manager: FragmentManager, tag: String?,playlist: Playlist)")
    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }

    fun show(manager: FragmentManager, tag: String?,playlist: Playlist) {
        this.playlist = playlist
        super.show(manager, tag)
    }

}