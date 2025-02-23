package com.sf.musicapp.view.fragment

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sf.musicapp.R
import com.sf.musicapp.adapter.ArtistAdapter
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.databinding.FragmentItemSavedBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.view.activity.viewmodel.DBViewModel
import com.sf.musicapp.view.activity.viewmodel.SortOrder
import com.sf.musicapp.view.activity.viewmodel.SortType
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import com.sf.musicapp.view.custom.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ArtistItemSavedFragment : BaseBottomSheetFragment<FragmentItemSavedBinding>() {
    @Inject
    lateinit var playerHelper: PlayerHelper

    private lateinit var adapter: ArtistAdapter
    private val dbViewModel: DBViewModel by activityViewModels()
    private var scope: Job?=null


    override fun getViewBinding(): FragmentItemSavedBinding {
        return FragmentItemSavedBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        binding.songs.text = requireContext().getString(R.string.bookmark)

        val artistPickerFragment = ArtistPickerFragment()
        adapter = ArtistAdapter{
            artistPickerFragment.show(parentFragmentManager,artistPickerFragment.tag,it)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireActivity(),2)
            post{
                addItemDecoration(ItemDecoration.get(resources))
            }
        }








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
            dbViewModel.artists.collectLatest {
                adapter.setData(it)
            }
        }
    }
}