package com.sf.musicapp.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.sf.musicapp.adapter.base.BasePagingAdapter
import com.sf.musicapp.databinding.ItemSmallLayoutBinding
import com.sf.musicapp.databinding.ItemSmallShimmerLayoutBinding

abstract class ItemPagingAdapter<T: Any> (
    DIFF_CALLBACK: DiffUtil.ItemCallback<T>,
    private val itemClick:(T)->Unit
): BasePagingAdapter<T>(DIFF_CALLBACK) {
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
        data: T
    ) {
        if (binding is ItemSmallLayoutBinding){
            bind(binding,data)
            binding.root.setOnClickListener{
                itemClick(data)
            }

        }
    }

    abstract fun bind(
        binding: ItemSmallLayoutBinding,
        data:T
    )
}