package com.sf.musicapp.adapter.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewbinding.ViewBindings
import okhttp3.internal.notify

// base paging adapter cho các loại dữ liệu
abstract class BasePagingAdapter<T:Any>(
    diffCallback: DiffUtil.ItemCallback<T>
): PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    companion object{
        private const val VIEW_TYPE_DATA = 1
        private const val VIEW_TYPE_PLACEHOLDER = 0
    }

    private var showPlaceHolder = true
    private var hasData = false
    init{
        addLoadStateListener {loadState->
            showPlaceHolder = super.itemCount == 0
            if (!showPlaceHolder && !hasData){
                notifyDataSetChanged()
                hasData = true
            }
        }
    }

    abstract fun getDataViewBinding(layoutInflater: LayoutInflater,parent: ViewGroup): ViewBinding
    abstract fun getPlaceholderViewBinding(layoutInflater: LayoutInflater,parent: ViewGroup): ViewBinding

    abstract fun bindData(binding: ViewBinding,data:T)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) bindData(holder.binding,getItem(position)!!)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == VIEW_TYPE_DATA) return DataViewHolder(
            getDataViewBinding(layoutInflater,parent)
        )
        return PlaceholderViewHolder(
            getPlaceholderViewBinding(layoutInflater,parent)
        )
    }

    private class PlaceholderViewHolder(
        private val binding: ViewBinding
    ): RecyclerView.ViewHolder(binding.root)

    private class DataViewHolder(
        val binding: ViewBinding
    ): RecyclerView.ViewHolder(binding.root)


    override fun getItemViewType(position: Int): Int {
        return if(showPlaceHolder) VIEW_TYPE_PLACEHOLDER else VIEW_TYPE_DATA
    }

    override fun getItemCount(): Int {
        return if(showPlaceHolder) 8 else super.getItemCount()
    }

}