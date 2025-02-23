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
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.databinding.FragmentAlbumBinding
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
class AlbumFragment : BaseFragment<FragmentAlbumBinding>() {
    override var isTerminalBackKeyActive: Boolean = false
    private lateinit var adapter: ItemPagingAdapter<Album>
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon:ImageView
    private lateinit var albumItemSavedFragment: AlbumItemSavedFragment

    private val appViewModel: AppViewModel by activityViewModels()

    override fun getDataBinding(): FragmentAlbumBinding {
        return FragmentAlbumBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.hint = "Search"
        searchEditText.setHintTextColor(requireContext().getColor(R.color.not_select))
        searchIcon = binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val albumPickerFragment = AlbumPickerFragment()
        adapter = object: ItemPagingAdapter<Album>(
            DiffCallBack.album,
            itemClick = {
                albumPickerFragment.show(parentFragmentManager,albumPickerFragment.tag,it)
            }){
                override fun bind(
                    binding: ItemSmallLayoutBinding,
                    data: Album
                ) {
                    binding.itemTitle.text = data.name.truncate(Limits.ALBUM_CHAR_LIMIT)
                    binding.itemAuthor.text = DateConverter.fromDate(data.releaseDate)
                    binding.itemImg.loadImg(data.image,R.drawable.playlist)
                }}

        binding.songsRecyclerView.adapter = adapter
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        albumItemSavedFragment = AlbumItemSavedFragment()
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
            albumItemSavedFragment.show(parentFragmentManager,albumItemSavedFragment.tag)
        }
    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            launch{
                appViewModel.albumSearchPager.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

    }

}