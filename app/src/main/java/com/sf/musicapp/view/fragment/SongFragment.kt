package com.sf.musicapp.view.fragment

import android.graphics.Color
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.FragmentSongBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.view.activity.MainActivity
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sf.musicapp.R

@AndroidEntryPoint
class SongFragment : BaseFragment<FragmentSongBinding>() {
    override var isTerminalBackKeyActive: Boolean = false
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView
    @Inject
    lateinit var playerHelper: PlayerHelper
    private val appViewModel: AppViewModel by activityViewModels()
    private var tracks = listOf<Track>()
    private lateinit var adapter: ItemAdapter
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment

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
        adapter = ItemAdapter{
            playerHelper.playNewTrack(it)
            playMusicBottomFragment.show(parentFragmentManager,playMusicBottomFragment.tag)
        }
        binding.songsRecyclerView.adapter = adapter
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun addEvent() {
        super.addEvent()
        binding.searchView.setOnClickListener{
            binding.searchView.isIconified = false
        }
        searchEditText.setOnEditorActionListener {_,actionId,_->
            if (actionId== EditorInfo.IME_ACTION_SEARCH){
                val query = searchEditText.text.toString()
                if (query.isNotEmpty()){
                    lifecycleScope.launch{
                        tracks= appViewModel.searchTrack(query,1)
                        if (tracks.isNotEmpty()){
                            binding.favouriteButton.visibility = View.GONE
                            binding.favouriteText.visibility = View.GONE
                            adapter.setData(tracks)
                            binding.songsRecyclerView.visibility = View.VISIBLE
                        }
                    }
                }
                true
            }
            false
        }
    }
}