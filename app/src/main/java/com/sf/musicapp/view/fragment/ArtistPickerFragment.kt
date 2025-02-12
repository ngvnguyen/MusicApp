package com.sf.musicapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sf.musicapp.R
import com.sf.musicapp.adapter.AlbumAdapter
import com.sf.musicapp.adapter.ItemAdapter
import com.sf.musicapp.adapter.paging.SmallItemAdapter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.FragmentArtistPickerBinding
import com.sf.musicapp.utils.Jamendo
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.shareText
import com.sf.musicapp.view.activity.MainActivity
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ArtistPickerFragment : BaseBottomSheetFragment<FragmentArtistPickerBinding>() {

    @Inject
    lateinit var playerHelper: PlayerHelper
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var artist: Artist
    private lateinit var trackAdapter: ItemAdapter
    private lateinit var albumAdapter: AlbumAdapter
    private var tracks = listOf<Track>()
    private var albums = listOf<Album>()
    private lateinit var playMusicBottomFragment: PlayMusicBottomFragment
    private lateinit var trackAdapterPager: SmallItemAdapter

    override fun getViewBinding(): FragmentArtistPickerBinding {
        return FragmentArtistPickerBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        playMusicBottomFragment = (requireActivity() as MainActivity).playMusicBottomFragment
        binding.artistName.text = artist.name
        binding.joinDate.text = artist.joinDate
        //
        binding.image.loadImg(artist.imageUrl,R.drawable.sync)
        trackAdapter = ItemAdapter{
            playerHelper.playNewTrack(it)
            playerHelper.play()
            playMusicBottomFragment.show(parentFragmentManager, "play music")
        }
        trackAdapterPager = SmallItemAdapter{
            playerHelper.playNewTrack(it)
            playMusicBottomFragment.show(parentFragmentManager, playMusicBottomFragment.tag)
        }
        binding.trackRecyclerView.adapter = trackAdapter
        binding.trackRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val albumPickerFragment = AlbumPickerFragment()
        albumAdapter = AlbumAdapter{
            albumPickerFragment.show(parentFragmentManager,albumPickerFragment.tag,it)
        }
        binding.albumRecyclerView.adapter = albumAdapter
        binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launch{
            launch{
                tracks = appViewModel.getAllTrackByArtistId(artist.id)

                if (tracks.isEmpty()){
                    binding.noTrack.visibility = View.VISIBLE
                }else binding.noTrack.visibility = View.INVISIBLE

                if (tracks.size> Limits.MAX_ITEM) {
                    trackAdapter.setData(tracks.take(Limits.MAX_ITEM))
                    binding.tracksViewAll.visibility = View.VISIBLE
                }
                else{
                    trackAdapter.setData(tracks)
                    binding.tracksViewAll.visibility = View.INVISIBLE
                }
            }
            launch{
                albums = appViewModel.getAllAlbumByArtistId(artist.id)
                albumAdapter.setData(albums)
                if (albums.isEmpty()){
                    binding.noAlbum.visibility = View.VISIBLE
                }else binding.noAlbum.visibility = View.INVISIBLE
            }


        }
    }

    override fun addEvent() {
        super.addEvent()
        binding.playButton.setOnClickListener{
            playerHelper.insertPlaylist(tracks)
            playerHelper.play()
            playMusicBottomFragment.show(parentFragmentManager,playMusicBottomFragment.tag)
        }
        binding.shareButton.setOnClickListener{
            context?.shareText(Jamendo.getShareArtistUrl(artist.id))
        }
        binding.tracksViewAll.setOnClickListener{
            appViewModel.setTracksPager(tracks)
            binding.trackRecyclerView.adapter = trackAdapterPager
            lifecycleScope.launch{
                launch{
                    appViewModel.trackSetPager.collectLatest{
                        trackAdapterPager.submitData(it)
                    }
                }
            }
            binding.tracksViewAll.visibility = View.GONE
        }
    }

    @Deprecated("Use show(manager: FragmentManager, tag: String?,artist: Artist)")
    override fun show(manager: FragmentManager, tag: String?) {
        //super.show(manager, tag)
    }

    fun show(manager: FragmentManager, tag: String?,artist: Artist){
        this.artist = artist
        super.show(manager,tag)
    }

}