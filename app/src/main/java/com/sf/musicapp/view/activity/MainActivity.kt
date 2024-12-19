package com.sf.musicapp.view.activity

import android.content.ComponentName
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.OptIn
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.sf.musicapp.databinding.ActivityMainBinding
import com.sf.musicapp.view.custom.VerticalTextView
import com.sf.musicapp.view.base.BaseActivity
import com.sf.musicapp.R
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.service.MusicService
import com.sf.musicapp.utils.DialogUtils
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.fragment.PlayMusicBottomFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var navigationItems: List<NavigationItem> = listOf()
    private var expandButton = ExpandButton.COLLAPSE
    private lateinit var navController: NavController
    private val viewModel: AppViewModel by viewModels()
    private lateinit var mediaController: MediaController
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private var track: Track?= null


    @Inject
    lateinit var player: ExoPlayer
    lateinit var playMusicBottomFragment: PlayMusicBottomFragment
        private set


    override fun onResume() {
        super.onResume()
    }

    override fun getDataBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViewModel() {
        super.initViewModel()
    }


    override fun initView() {
        super.initView()
        navigationItems = getNavigationItem()
        navController = (supportFragmentManager.findFragmentById(
                binding.fragmentContainerView.id
            ) as NavHostFragment).navController

        binding.playerView.player = player
        playMusicBottomFragment = PlayMusicBottomFragment{track->
            if (binding.musicBar.visibility == View.GONE){
                binding.musicBar.visibility = View.VISIBLE
            }
            this.track = track
            binding.artist.text = track.artistName
            binding.title.text = track.name
        }
        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_ONE
        val i = Intent(this, MusicService::class.java)
        startForegroundService(i)
    }

    override fun addEvent() {
        super.addEvent()
        setupNavigation()
        binding.buttonExpand.setOnClickListener{
            expandEvent()
        }
        binding.musicBar.setOnClickListener{
            playMusicBottomFragment.show(supportFragmentManager,"play music",track,
                PlayMusicBottomFragment.ON_GOING)
        }
        binding.playButton.setOnClickListener{
            if (player.isPlaying){
                player.pause()
                binding.playButton.setIconResource(R.drawable.play)
            }else{
                player.play()
                binding.playButton.setIconResource(R.drawable.pause)
            }
        }
    }

    override fun addObservers() {
        super.addObservers()
    }

    override fun initData() {
        super.initData()
    }

    private fun getButtonExpandRefresh(): ExpandButton{
        return if(binding.toolbar.visibility == View.VISIBLE) ExpandButton.EXPAND
            else ExpandButton.COLLAPSE
    }
    private fun expandEvent(){
        expandButton = getButtonExpandRefresh()
        binding.buttonExpand.setImageResource(expandButton.resId)
        binding.toolbar.visibility = expandButton.visibility
    }

    private fun setupNavigation(){
        navigationItems.forEach { navigationItem->
            navigationItem.layout.setOnClickListener{
                navigationItem.title.setTextColor(getColor(R.color.white))
                navigationItem.imgView.visibility = View.VISIBLE
                hideNavigationIconExcept(navigationItem.layout.id)
                navController.currentDestination?.let {
                    if (it.id!= navigationItem.fragmentId) {
                        navController.navigate(navigationItem.fragmentId)
                    }
                }


            }

        }
    }

    private fun hideNavigationIconExcept(resId:Int){
        navigationItems.forEach {navigationItem->
            if (navigationItem.layout.id!=resId){
                navigationItem.imgView.visibility= View.GONE
                navigationItem.title.setTextColor(getColor(R.color.not_select))
            }
        }
    }




    fun getNavigationItem(): List<NavigationItem>{
        return if (navigationItems.isNotEmpty()) navigationItems else listOf(
            NavigationItem(
                binding.quickPicNav,
                binding.imgQuickPick,
                binding.textQuickPick,
                R.id.quickPickFragment
            ),
            NavigationItem(
                binding.songsNav,
                binding.imgSong,
                binding.textSongs,
                R.id.songFragment
            ),
            NavigationItem(
                binding.playlistsNav,
                binding.imgPlaylist,
                binding.textPlaylist,
                R.id.playlistFragment
            ),
            NavigationItem(
                binding.artistsNav,
                binding.imgArtist,
                binding.textArtist,
                R.id.artistFragment
            ),
            NavigationItem(
                binding.albumsNav,
                binding.imgAlbum,
                binding.textAlbum,
                R.id.albumFragment
            )
        )
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(
            this,
            ComponentName(this, MusicService::class.java))

        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }

    override fun onStop() {
        super.onStop()
        MediaController.releaseFuture(controllerFuture)
    }

}
data class NavigationItem(
    val layout: ConstraintLayout,
    val imgView: ImageView,
    val title: VerticalTextView,
    val fragmentId:Int
)
enum class ExpandButton(@DrawableRes val resId: Int,val visibility:Int){
    EXPAND(R.drawable.chevron_forward,View.GONE),
    COLLAPSE(R.drawable.chevron_back, View.VISIBLE)
}