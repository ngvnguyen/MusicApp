package com.sf.musicapp.view.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.databinding.FragmentItemSavedBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.view.activity.viewmodel.DBViewModel
import com.sf.musicapp.view.activity.viewmodel.SortOrder
import com.sf.musicapp.view.activity.viewmodel.SortType
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumItemSavedFragment : BaseBottomSheetFragment<FragmentItemSavedBinding>() {
    @Inject
    lateinit var playerHelper: PlayerHelper

    private lateinit var adapter: ItemAdapter<Album>
    private val dbViewModel: DBViewModel by activityViewModels()
    private var scope: Job?=null


    override fun getViewBinding(): FragmentItemSavedBinding {
        return FragmentItemSavedBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        binding.songs.text = requireContext().getString(R.string.bookmark)

        val albumPickerFragment = AlbumPickerFragment()
        adapter = object : ItemAdapter<Album>(
            itemClick = {
                albumPickerFragment.show(parentFragmentManager,albumPickerFragment.tag,it)
            }){
            override fun bind(
                binding: ItemSmallLayoutBinding,
                data: Album
            ) {
                binding.itemAuthor.text = data.artistName
                binding.itemTitle.text = data.name
                binding.itemImg.loadImg(data.image, R.drawable.server)
            } }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

    }

    override fun addEvent() {
        super.addEvent()
        binding.sortByName.setOnClickListener{
            if (binding.sortByName.isChecked){
                dbViewModel.setSortType(SortType.NAME)
            }else dbViewModel.setSortType(SortType.NONE)
        }
        binding.sortByDate.setOnClickListener {
            if (binding.sortByDate.isChecked){
                dbViewModel.setSortType(SortType.DATE)
            }else dbViewModel.setSortType(SortType.NONE)
        }
        binding.sortBt.setOnClickListener{
            if (binding.sortBt.isCheckable)
                dbViewModel.toggleSortOrder()
        }

    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            launch{
                dbViewModel.sortState.collectLatest {
                    binding.sortByDate.isChecked = it.sortType == SortType.DATE
                    binding.sortByName.isChecked = it.sortType == SortType.NAME
                    binding.sortBt.isChecked = it.sortOrder == SortOrder.DESC && it.sortType != SortType.NONE
                    binding.sortBt.isCheckable = it.sortType != SortType.NONE
                    collectData()
                }
            }
        }
    }

    fun collectData(){
        scope?.cancel()
        scope = lifecycleScope.launch{
            dbViewModel.albums.collectLatest {
                adapter.setData(it)
            }
        }
    }
}