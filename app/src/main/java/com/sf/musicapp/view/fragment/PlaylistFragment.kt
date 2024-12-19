package com.sf.musicapp.view.fragment

import com.sf.musicapp.databinding.FragmentPlaylistBinding
import com.sf.musicapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>() {
    override var isTerminalBackKeyActive: Boolean = false

    override fun getDataBinding(): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(layoutInflater)
    }

}