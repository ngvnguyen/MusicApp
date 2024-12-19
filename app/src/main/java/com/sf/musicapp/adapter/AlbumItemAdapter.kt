package com.sf.musicapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sf.musicapp.adapter.base.BaseAdapter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.databinding.ItemAlbumLayoutBinding
import com.sf.musicapp.databinding.ItemAlbumShimmerLayoutBinding

class AlbumItemAdapter(
    private val itemClick:()->Unit={}
): BaseAdapter<ItemAlbumLayoutBinding, ItemAlbumShimmerLayoutBinding, Album>(
    dataHolderInflater = {li,vg,at->
        ItemAlbumLayoutBinding.inflate(li,vg,at)
    },
    placeHolderInflater = {li,vg,at->
        ItemAlbumShimmerLayoutBinding.inflate(li,vg,at)
    }
) {




    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        holder.itemView.setOnClickListener{
            itemClick()
        }
    }
}