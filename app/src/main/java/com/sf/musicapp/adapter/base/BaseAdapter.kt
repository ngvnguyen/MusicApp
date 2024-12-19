package com.sf.musicapp.adapter.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T: ViewBinding,PH:ViewBinding,DATA>(
    private val dataHolderInflater:(LayoutInflater, ViewGroup?,Boolean)->T,
    private val placeHolderInflater:(LayoutInflater, ViewGroup?, Boolean)->PH
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = mutableListOf<DATA>()
    private var isLoading = true

    fun setData(data: List<DATA>){
        this.data.addAll(data)
        isLoading = false
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (isLoading) LoadingViewHolder(
            placeHolderInflater(layoutInflater,parent,false)
        ) else LoadedViewHolder(
            dataHolderInflater(layoutInflater,parent,false)
        )
    }

//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is BaseAdapter<T, PH, DATA>.LoadedViewHolder)
//            bind(holder.binding,position)
//    }
//
//    abstract fun bind(binding:T,position: Int)

    override fun getItemCount(): Int {
        return if (isLoading) 8 else data.size
    }

    inner class LoadingViewHolder(
        private val binding: PH
    ): RecyclerView.ViewHolder(binding.root){

    }
    inner class LoadedViewHolder(
        val binding: T
    ): RecyclerView.ViewHolder(binding.root){

    }



}