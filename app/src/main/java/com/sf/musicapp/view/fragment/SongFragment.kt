package com.sf.musicapp.view.fragment

import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.databinding.FragmentSongBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.view.activity.MainActivity
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sf.musicapp.R
import com.sf.musicapp.adapter.paging.TrackItemAdapter
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SongFragment : BaseFragment<FragmentSongBinding>() {
    override var isTerminalBackKeyActive: Boolean = false
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView
    @Inject
    lateinit var playerHelper: PlayerHelper
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var adapter: TrackItemAdapter
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment
    private lateinit var itemSavedFragment: TrackItemSavedFragment



    override fun getDataBinding(): FragmentSongBinding {
        return FragmentSongBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

        // set màu cho searchBar
        searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.hint = "Search"
        searchEditText.setHintTextColor(requireContext().getColor(R.color.not_select))
        searchIcon = binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        playMusicBottomFragment = (requireActivity() as MainActivity).playMusicBottomFragment
        adapter = TrackItemAdapter{
            playerHelper.playNewTrack(it)
            playMusicBottomFragment.show(parentFragmentManager,playMusicBottomFragment.tag)
        }
        binding.songsRecyclerView.adapter = adapter
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        itemSavedFragment = TrackItemSavedFragment()
    }

    override fun addEvent() {
        super.addEvent()
        binding.searchView.setOnClickListener{
            binding.searchView.isIconified = false
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length > 1){
                    appViewModel.search(query)
                    binding.favouriteButton.visibility = View.GONE
                    binding.favouriteText.visibility = View.GONE
                    binding.songsRecyclerView.visibility = View.VISIBLE
                    return false
                }else {
                    Toast.makeText(requireActivity(),"The minimum character is 2", Toast.LENGTH_SHORT).show()
                    return true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })


        binding.favouriteButton.setOnClickListener{
            itemSavedFragment.show(parentFragmentManager,itemSavedFragment.tag)
        }
    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            launch{
                appViewModel.trackSearchPager.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

    }


}