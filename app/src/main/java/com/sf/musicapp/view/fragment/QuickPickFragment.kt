package com.sf.musicapp.view.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sf.musicapp.adapter.paging.AlbumItemAdapter
import com.sf.musicapp.adapter.paging.ArtistItemAdapter
import com.sf.musicapp.adapter.paging.PlaylistItemAdapter
import com.sf.musicapp.adapter.paging.TrackItemAdapter
import com.sf.musicapp.databinding.FragmentQuickPickBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.view.activity.MainActivity
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuickPickFragment() : BaseFragment<FragmentQuickPickBinding>(){
    override var isTerminalBackKeyActive: Boolean = false
    override fun getDataBinding(): FragmentQuickPickBinding {
        return FragmentQuickPickBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var playerHelper: PlayerHelper
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment
    private val viewModel: AppViewModel by activityViewModels()

    override fun initViewModel() {
        super.initViewModel()
    }

    override fun initData() {
        super.initData()

        playMusicBottomFragment = (requireActivity() as MainActivity).playMusicBottomFragment

        //quick pick
        val trackItemAdapter = TrackItemAdapter{ track->
            playerHelper.playNewTrack(track)
            playMusicBottomFragment.show(parentFragmentManager,playMusicBottomFragment.tag)
        }
        binding.suggestRecyclerView.adapter = trackItemAdapter
        binding.suggestRecyclerView.layoutManager = GridLayoutManager(requireActivity(),4, RecyclerView.HORIZONTAL,false)
        lifecycleScope.launch{
            viewModel.trackRecommendedPager.collectLatest {
                trackItemAdapter.submitData(it)
            }
        }


        //album
        val albumPickerFragment = AlbumPickerFragment()
        val albumItemAdapter = AlbumItemAdapter(){
            albumPickerFragment.show(parentFragmentManager,albumPickerFragment.tag,it)
        }
        binding.relatedAlbumsRecyclerView.adapter = albumItemAdapter
        binding.relatedAlbumsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL,false)
        lifecycleScope.launch{
            viewModel.albumRecommendedPager.collectLatest {
                albumItemAdapter.submitData(it)
            }
        }

        //artist
        val artistPickerFragment = ArtistPickerFragment()
        val artistItemAdapter = ArtistItemAdapter(){
            artistPickerFragment.show(parentFragmentManager,artistPickerFragment.tag,it)
        }
        lifecycleScope.launch{
            viewModel.artistRecommendedPager.collectLatest {
                artistItemAdapter.submitData(it)
            }
        }

        binding.similarArtistsRecyclerView.adapter = artistItemAdapter
        binding.similarArtistsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL,false)

        //playlist
        val playlistPickerFragment = PlaylistPickerFragment()
        val playlistItemAdapter = PlaylistItemAdapter(){
            playlistPickerFragment.show(parentFragmentManager,playlistPickerFragment.tag,it)
        }
        binding.mayLikesRecyclerView.adapter = playlistItemAdapter
        binding.mayLikesRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL,false)
        lifecycleScope.launch{
            viewModel.playlistRecommendedPager.collectLatest {
                playlistItemAdapter.submitData(it)
            }
        }
    }
}