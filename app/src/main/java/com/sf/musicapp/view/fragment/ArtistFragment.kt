package com.sf.musicapp.view.fragment

import com.sf.musicapp.databinding.FragmentArtistBinding
import com.sf.musicapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistFragment : BaseFragment<FragmentArtistBinding>() {
    override var isTerminalBackKeyActive: Boolean = false
    override fun getDataBinding(): FragmentArtistBinding {
        return FragmentArtistBinding.inflate(layoutInflater)
    }

}