package com.sf.musicapp.view.activity

import android.content.ComponentName
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.common.util.concurrent.ListenableFuture
import com.sf.musicapp.databinding.ActivityMainBinding
import com.sf.musicapp.view.custom.VerticalTextView
import com.sf.musicapp.view.base.BaseActivity
import com.sf.musicapp.R
import com.sf.musicapp.service.MusicService
import com.sf.musicapp.utils.PlayerHelper
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.view.activity.viewmodel.AppViewModel
import com.sf.musicapp.view.fragment.AlbumPickerFragment
import com.sf.musicapp.view.fragment.PlayMusicBottomFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var navigationItems: List<NavigationItem> = listOf()
    private var expandButton = ExpandButton.COLLAPSE
    private lateinit var navController: NavController

    // WARNING: khởi tạo viewModel, ở các fragment có sử dụng nên xóa dòng này sẽ gây lỗi
    private val viewModel: AppViewModel by viewModels()

    private lateinit var mediaController: MediaController
    private lateinit var controllerFuture: ListenableFuture<MediaController>

    private lateinit var sessionToken: SessionToken

    @Inject
    lateinit var playerHelper: PlayerHelper

    lateinit var playMusicBottomFragment: PlayMusicBottomFragment
        private set

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


        playMusicBottomFragment = PlayMusicBottomFragment{
            if (binding.musicBar.visibility == View.GONE){
                binding.musicBar.visibility = View.VISIBLE
            }
        }
    }

    override fun addEvent() {
        super.addEvent()
        setupNavigation()
        binding.buttonExpand.setOnClickListener{
            expandEvent()
        }
        binding.musicBar.setOnClickListener{
            playMusicBottomFragment.show(supportFragmentManager,playMusicBottomFragment.tag)
        }
        binding.playButton.setOnClickListener{
            if (playerHelper.isPlaying.value) playerHelper.pause()
                else playerHelper.play()
        }

    }

    override fun addObservers() {
        super.addObservers()
        lifecycleScope.launch{
            // dùng sateFLow để theo dõi hoạt động phát nhạc
            launch{

                playerHelper.isPlaying.collectLatest {isPlaying->
                    if (isPlaying){
                        binding.playButton.setIconResource(R.drawable.pause)
                    }else{
                        if(playerHelper.isEnded.value.not())
                            binding.playButton.setIconResource(R.drawable.play)
                    }
                }
            }
            launch{
                playerHelper.currentTrack.collectLatest { track->
                    track?.let {
                        with(it){
                            binding.artist.text = artistName
                            binding.title.text = name
                            binding.playerView.loadImg(image)
                        }
                    }
                }
            }
            launch{
                playerHelper.isEnded.collectLatest {
                    if (it){
                        binding.playButton.setIconResource(R.drawable.replay)
                    }
                }
            }
        }

    }

    override fun initData() {
        super.initData()
    }
    // lấy trạng thái hiện tại của nút mở rộng
    private fun getButtonExpandRefresh(): ExpandButton{
        return if(binding.toolbar.visibility == View.VISIBLE) ExpandButton.EXPAND
            else ExpandButton.COLLAPSE
    }
    // sự kiện click vào nút mở rộng
    private fun expandEvent(){
        expandButton = getButtonExpandRefresh()
        binding.buttonExpand.setImageResource(expandButton.resId)
        binding.toolbar.visibility = expandButton.visibility
    }
    // thiết lập navigation bar
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
    // ẩn các navigation item không được chọn
    private fun hideNavigationIconExcept(resId:Int){
        navigationItems.forEach {navigationItem->
            if (navigationItem.layout.id!=resId){
                navigationItem.imgView.visibility= View.GONE
                navigationItem.title.setTextColor(getColor(R.color.not_select))
            }
        }
    }



    // danh sách các navigation item hiện có
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //tạo session token và media controller cho media session service
        sessionToken = SessionToken(
            this,
            ComponentName(this, MusicService::class.java))

        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaController.releaseFuture(controllerFuture)
    }

}

// định nghĩa các nút navigation
data class NavigationItem(
    val layout: ConstraintLayout,
    val imgView: ImageView,
    val title: VerticalTextView,
    val fragmentId:Int
)

// enum cho nút mở rộng
enum class ExpandButton(@DrawableRes val resId: Int,val visibility:Int){
    EXPAND(R.drawable.chevron_forward,View.GONE),
    COLLAPSE(R.drawable.chevron_back, View.VISIBLE)
}