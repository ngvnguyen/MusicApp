package com.sf.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.sf.musicapp.R
import com.sf.musicapp.adapter.base.BaseAdapter
import com.sf.musicapp.data.model.Track
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.databinding.ItemSmallShimmerLayoutBinding
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.loadImg
import com.sf.musicapp.utils.truncate

abstract class ItemAdapter<T>(
    private val itemClick:(T)->Unit={}
): BaseAdapter<T>() {
    override fun getDataViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemSmallLayoutBinding.inflate(layoutInflater, parent, false)
    }

    override fun getPlaceholderViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemSmallShimmerLayoutBinding.inflate(layoutInflater, parent, false)
    }


    override fun bindData(
        binding: ViewBinding,
        data: T
    ) {
        if (binding is ItemSmallLayoutBinding) {
            bind(binding,data)
            binding.root.setOnClickListener {
                itemClick(data)
            }

        }
    }

    abstract fun bind(
        binding: ItemSmallLayoutBinding,
        data: T
    )
}