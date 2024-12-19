package com.sf.musicapp.view.fragment

import com.sf.musicapp.databinding.FragmentSongBinding
import com.sf.musicapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongFragment : BaseFragment<FragmentSongBinding>() {
    override var isTerminalBackKeyActive: Boolean = false

    override fun getDataBinding(): FragmentSongBinding {
        return FragmentSongBinding.inflate(layoutInflater)
    }

}