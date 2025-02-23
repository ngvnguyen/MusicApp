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
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sf.musicapp.R
import com.sf.musicapp.adapter.diff.DiffCallBack
import com.sf.musicapp.adapter.paging.ArtistItemAdapter
import com.sf.musicapp.adapter.paging.ItemPagingAdapter
import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.databinding.FragmentArtistBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.truncate
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.base.BaseFragment
import com.sf.musicapp.view.custom.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistFragment : BaseFragment<FragmentArtistBinding>() {
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var adapter: ArtistItemAdapter
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var artistItemSavedFragment: ArtistItemSavedFragment

    override var isTerminalBackKeyActive: Boolean = false
    override fun getDataBinding(): FragmentArtistBinding {
        return FragmentArtistBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.hint = "Search"
        searchEditText.setHintTextColor(requireContext().getColor(R.color.not_select))
        searchIcon = binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val artistPickerFragment = ArtistPickerFragment()
        adapter = ArtistItemAdapter{
            artistPickerFragment.show(parentFragmentManager,artistPickerFragment.tag,it)
        }
        binding.songsRecyclerView.adapter = adapter
        binding.songsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireActivity(),2)
            post{
                addItemDecoration(ItemDecoration.get(resources))
            }
        }

        artistItemSavedFragment = ArtistItemSavedFragment()
    }

    override fun addEvent() {
        super.addEvent()
        binding.searchView.setOnClickListener{
            binding.searchView.isIconified = false
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length > 1){
                    lifecycleScope.launch{
                        adapter.submitData(PagingData.empty())
                        delay(100)
                        appViewModel.search(query)
                    }
                    binding.savedButton.visibility = View.GONE
                    binding.savedText.visibility = View.GONE
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
            artistItemSavedFragment.show(parentFragmentManager,artistItemSavedFragment.tag)
        }
    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            launch{
                appViewModel.artistSearchPager.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

    }
}