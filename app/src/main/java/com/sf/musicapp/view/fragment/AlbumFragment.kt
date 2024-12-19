package com.sf.musicapp.view.fragment

import com.sf.musicapp.databinding.FragmentAlbumBinding
import com.sf.musicapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : BaseFragment<FragmentAlbumBinding>() {
    override var isTerminalBackKeyActive: Boolean = false

    override fun getDataBinding(): FragmentAlbumBinding {
        return FragmentAlbumBinding.inflate(layoutInflater)
    }

}