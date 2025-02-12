package com.sf.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.sf.musicapp.R
import com.sf.musicapp.adapter.base.BaseAdapter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.ItemAlbumLayoutBinding
import com.sf.musicapp.databinding.ItemAlbumShimmerLayoutBinding
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.databinding.ItemSmallShimmerLayoutBinding
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.truncate

class AlbumAdapter(
    private val onItemClick:(Album)->Unit
): BaseAdapter<Album>() {
    override fun getDataViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemSmallLayoutBinding.inflate(layoutInflater,parent,false)
    }

    override fun getPlaceholderViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemSmallShimmerLayoutBinding.inflate(layoutInflater,parent,false)
    }


    override fun bindData(
        binding: ViewBinding,
        data: Album
    ) {
        if (binding is ItemSmallLayoutBinding){
            binding.itemAuthor.text = data.releaseDate //
            binding.itemTitle.text = data.name
            binding.itemImg.loadImg(data.image,R.drawable.server)
            binding.root.setOnClickListener{
                onItemClick(data)
            }

        }
    }

}