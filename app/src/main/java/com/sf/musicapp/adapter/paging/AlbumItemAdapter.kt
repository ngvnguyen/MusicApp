package com.sf.musicapp.adapter.paging

import com.sf.musicapp.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.sf.musicapp.adapter.base.BasePagingAdapter
import com.sf.musicapp.adapter.diff.DiffCallBack
import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.databinding.ItemAlbumLayoutBinding
import com.sf.musicapp.databinding.ItemAlbumShimmerLayoutBinding
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.truncate

class AlbumItemAdapter(
    private val onItemClick:(Album)->Unit={}
): BasePagingAdapter<Album>(DiffCallBack.album){
    override fun getDataViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemAlbumLayoutBinding.inflate(layoutInflater,parent,false)
    }

    override fun getPlaceholderViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemAlbumShimmerLayoutBinding.inflate(layoutInflater,parent,false)
    }

    override fun bindData(
        binding: ViewBinding,
        data: Album
    ) {
        if (binding is ItemAlbumLayoutBinding){
            binding.root.setOnClickListener{
                onItemClick(data)
            }
            binding.title.text = data.name.truncate(Limits.ALBUM_CHAR_LIMIT)
            binding.date.text = DateConverter.fromDate(data.releaseDate)
            binding.img.loadImg(data.image,R.drawable.playlist)
        }
    }

}