package com.sf.musicapp.view.fragment

import android.graphics.Color
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.diff.DiffCallBack
import com.sf.musicapp.adapter.paging.ItemPagingAdapter
import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.databinding.FragmentPlaylistBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.truncate
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>() {
    override var isTerminalBackKeyActive: Boolean = false
    private lateinit var searchEditText:EditText
    private lateinit var searchIcon:ImageView
    private lateinit var adapter: ItemPagingAdapter<Playlist>
    private val appViewModel : AppViewModel by activityViewModels()
    private lateinit var playlistItemSavedFragment: PlaylistItemSavedFragment

    override fun getDataBinding(): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

        searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.hint = "Search"
        searchEditText.setHintTextColor(requireContext().getColor(R.color.not_select))
        searchIcon = binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val playlistPickerFragment = PlaylistPickerFragment()
        adapter = object: ItemPagingAdapter<Playlist>(
            DiffCallBack.playlist,
            itemClick = {
                playlistPickerFragment.show(parentFragmentManager,playlistPickerFragment.tag,it)
            }){
            override fun bind(
                binding: ItemSmallLayoutBinding,
                data: Playlist
            ) {
                binding.itemTitle.text = data.name
                binding.itemAuthor.text = DateConverter.fromDate(data.creationDate)
                binding.itemImg.setImageResource(R.drawable.playlist)
            }}

        binding.songsRecyclerView.adapter = adapter
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        playlistItemSavedFragment = PlaylistItemSavedFragment()
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
                    binding.savedText.visibility = View.GONE
                    binding.savedButton.visibility = View.GONE
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

        binding.savedButton.setOnClickListener{
            playlistItemSavedFragment.show(parentFragmentManager,playlistItemSavedFragment.tag)
        }
    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            launch{
                appViewModel.playlistSearchPager.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

    }

}