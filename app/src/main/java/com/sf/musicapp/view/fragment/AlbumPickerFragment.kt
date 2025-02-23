package com.sf.musicapp.view.fragment

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.adapter.paging.TrackItemAdapter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.FragmentAlbumPickerBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
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
class AlbumPickerFragment: BaseBottomSheetFragment<FragmentAlbumPickerBinding>() {
    @Inject
    lateinit var playerHelper: PlayerHelper
    private val dbViewModel: DBViewModel by activityViewModels()

    private lateinit var album: Album
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var adapter: ItemAdapter<Track>
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment
    private var tracks = listOf<Track>()
    private lateinit var adapterPager: TrackItemAdapter

    override fun getViewBinding(): FragmentAlbumPickerBinding {
        return FragmentAlbumPickerBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()

        playMusicBottomFragment = (requireActivity() as MainActivity).playMusicBottomFragment

        binding.image.loadImg(album.image,R.drawable.musical_notes)
        binding.albumName.text = album.name

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
        binding.albumRecyclerView.adapter = adapter
        binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launch{
            tracks = appViewModel.getAllTrackByAlbumId(album.id)

            if (tracks.isEmpty()){
                binding.noTrack.visibility = View.VISIBLE
            }else binding.noTrack.visibility = View.GONE

            if (tracks.size> Limits.MAX_ITEM) {
                adapter.setData(tracks.take(Limits.MAX_ITEM))
                binding.viewAll.visibility = View.VISIBLE
            }
            else {
                adapter.setData(tracks)
                binding.viewAll.visibility = View.GONE
            }


        }

        dbViewModel.setAlbumId(album.id)
    }

    override fun addEvent() {
        binding.playButton.setOnClickListener{
            lifecycleScope.launch{
                playerHelper.insertPlaylist(tracks)
                playMusicBottomFragment.show(parentFragmentManager, playMusicBottomFragment.tag)
                playerHelper.play()
            }
        }
        binding.viewAll.setOnClickListener{
            binding.albumRecyclerView.adapter = adapterPager
            lifecycleScope.launch{
                appViewModel.trackSetPager.collectLatest {
                    adapterPager.submitData(it)
                }
            }
            binding.viewAll.visibility = View.GONE
        }
        binding.shareButton.setOnClickListener{
            context?.shareText(album.shareUrl)
        }
        binding.saveButton.setOnClickListener{
            lifecycleScope.launch{
                if (dbViewModel.albumIsSaved.value){
                    dbViewModel.deleteSavedAlbum(album)
                }else dbViewModel.insertSavedAlbum(album)
            }
        }
    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            dbViewModel.albumIsSaved.collectLatest {
                binding.saveButton.setIconTintResource(if (it) R.color.blue else R.color.white)
            }

        }
    }

    override fun onStart() {
        super.onStart()


    }

    override fun initViewModel() {
        super.initViewModel()

    }

    @Deprecated(
        "Use show(manager, tag, album) instead",
        ReplaceWith("show(manager, tag, album)"),
        DeprecationLevel.ERROR
    )
    override fun show(manager: FragmentManager, tag: String?) {
        //super.show(manager, tag)
    }

    // dùng funtion thay cho hàm mặc định để set album
    fun show(manager: FragmentManager, tag: String?,album : Album){
        this.album= album
        super.show(manager,tag)
    }
}