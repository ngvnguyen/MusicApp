package com.sf.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.sf.musicapp.R
import com.sf.musicapp.adapter.base.BaseAdapter
import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.databinding.ItemArtistLayoutBinding
import com.sf.musicapp.databinding.ItemArtistShimmerLayoutBinding
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.truncate

class ArtistAdapter(
    private val onItemClick: (Artist) -> Unit
): BaseAdapter<Artist>() {
    override fun getDataViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemArtistLayoutBinding.inflate(layoutInflater,parent,false)
    }

    override fun getPlaceholderViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemArtistShimmerLayoutBinding.inflate(layoutInflater,parent,false)
    }

    override fun bindData(
        binding: ViewBinding,
        data: Artist
    ) {
        if (binding is ItemArtistLayoutBinding){
            binding.artist.text = data.name.truncate(Limits.ARTIST_CHAR_LIMIT)
            binding.subscribe.text = DateConverter.fromDate(data.joinDate)
            if(data.imageUrl.isNotEmpty())
                binding.img.loadImg(data.imageUrl,R.drawable.person)

            binding.root.setOnClickListener{
                onItemClick(data)
            }
        }
    }
}