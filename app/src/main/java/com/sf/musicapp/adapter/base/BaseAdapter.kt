package com.sf.musicapp.adapter.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sf.musicapp.adapter.base.BasePagingAdapter.DataViewHolder

abstract class BaseAdapter<T>()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = mutableListOf<T>()
    companion object{
        const val VIEW_TYPE_DATA = 1
        const val VIEW_TYPE_PLACEHOLDER = 0
    }
    private var viewType = VIEW_TYPE_PLACEHOLDER

    fun setData(data: List<T>){
        this.data.clear()
        this.data.addAll(data)
        viewType = VIEW_TYPE_DATA
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_PLACEHOLDER) PlaceHolderViewHolder(
            getPlaceholderViewBinding(layoutInflater,parent)
        ) else DataViewHolder(
            getDataViewBinding(layoutInflater,parent)
        )
    }

    abstract fun getDataViewBinding(layoutInflater: LayoutInflater,parent: ViewGroup): ViewBinding
    abstract fun getPlaceholderViewBinding(layoutInflater: LayoutInflater,parent: ViewGroup): ViewBinding

    abstract fun bindData(binding: ViewBinding,data:T)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) bindData(holder.binding,data[position])
    }
    override fun getItemCount(): Int {
        return if (viewType== VIEW_TYPE_PLACEHOLDER) 8 else data.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    private class PlaceHolderViewHolder(
        private val binding: ViewBinding
    ): RecyclerView.ViewHolder(binding.root){

    }
    private class DataViewHolder(
        val binding: ViewBinding
    ): RecyclerView.ViewHolder(binding.root){

    }



}