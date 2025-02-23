package com.sf.musicapp.view.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.FragmentItemSavedBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.view.activity.MainActivity
import com.sf.musicapp.view.activity.viewmodel.DBViewModel
import com.sf.musicapp.view.activity.viewmodel.SortOrder
import com.sf.musicapp.view.activity.viewmodel.SortState
import com.sf.musicapp.view.activity.viewmodel.SortType
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrackItemSavedFragment : BaseBottomSheetFragment<FragmentItemSavedBinding>() {
    @Inject
    lateinit var playerHelper: PlayerHelper

    private lateinit var adapter: ItemAdapter<Track>
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment
    private val dbViewModel: DBViewModel by activityViewModels()
    private var scope: Job?=null


    override fun getViewBinding(): FragmentItemSavedBinding {
        return FragmentItemSavedBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        playMusicBottomFragment = (requireActivity() as MainActivity).playMusicBottomFragment
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
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.playButton.visibility = View.VISIBLE
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

        binding.playButton.setOnClickListener{
            playerHelper.insertPlaylist(dbViewModel.tracks.value)
            playerHelper.play()
            playMusicBottomFragment.show(parentFragmentManager,playMusicBottomFragment.tag)
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
            dbViewModel.tracks.collectLatest {
                adapter.setData(it)
            }
        }
    }
}