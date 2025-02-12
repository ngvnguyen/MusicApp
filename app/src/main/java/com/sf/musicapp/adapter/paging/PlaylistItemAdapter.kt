package com.sf.musicapp.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.sf.musicapp.adapter.base.BasePagingAdapter
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.databinding.ItemPlaylistLayoutBinding
import com.sf.musicapp.databinding.ItemPlaylistShimmerLayoutBinding
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.truncate

class PlaylistItemAdapter(
    private val onItemClick:(Playlist)->Unit ={}
): BasePagingAdapter<Playlist>(DIFF_CALLBACK){
    override fun getDataViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemPlaylistLayoutBinding.inflate(layoutInflater,parent,false)
    }

    override fun getPlaceholderViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemPlaylistShimmerLayoutBinding.inflate(layoutInflater,parent,false)
    }

    override fun bindData(
        binding: ViewBinding,
        data: Playlist
    ) {
        if (binding is ItemPlaylistLayoutBinding){
            binding.title.text = data.name.truncate(Limits.PLAYLIST_CHAR_LIMIT)
            binding.artist.text = data.userName.truncate(Limits.PLAYLIST_CHAR_LIMIT)
            binding.root.setOnClickListener{
                onItemClick(data)
            }
        }
    }

    companion object{
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Playlist>(){
            override fun areItemsTheSame(
                oldItem: Playlist,
                newItem: Playlist
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Playlist,
                newItem: Playlist
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}