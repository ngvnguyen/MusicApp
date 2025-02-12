package com.sf.musicapp.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.sf.musicapp.adapter.base.BasePagingAdapter
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.databinding.ItemSmallShimmerLayoutBinding
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.R
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.truncate

class SmallItemAdapter(
    private val itemClick:(Track)->Unit={}
): BasePagingAdapter<Track>(DIFF_CALLBACK) {


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
        data: Track
    ) {
        if (binding is ItemSmallLayoutBinding){
            binding.itemAuthor.text = data.artistName.truncate(Limits.TRACK_CHAR_LIMIT)
            binding.itemTitle.text = data.name.truncate(Limits.TRACK_CHAR_LIMIT)
            binding.itemImg.loadImg(data.image,R.drawable.server)
            binding.root.setOnClickListener{
                itemClick(data)
            }

        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Track>(){
            override fun areItemsTheSame(
                oldItem: Track,
                newItem: Track
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Track,
                newItem: Track
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }


}