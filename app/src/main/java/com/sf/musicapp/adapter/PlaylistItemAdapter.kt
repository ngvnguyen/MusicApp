package com.sf.musicapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sf.musicapp.adapter.base.BaseAdapter
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.databinding.ItemPlaylistLayoutBinding
import com.sf.musicapp.databinding.ItemPlaylistShimmerLayoutBinding

class PlaylistItemAdapter: BaseAdapter<ItemPlaylistLayoutBinding, ItemPlaylistShimmerLayoutBinding, Playlist>(
    dataHolderInflater = {li,vg,at->
        ItemPlaylistLayoutBinding.inflate(li,vg,at)
    },
    placeHolderInflater = {li,vg,at->
        ItemPlaylistShimmerLayoutBinding.inflate(li,vg,at)
    }
) {
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

    }


}