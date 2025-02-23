package com.sf.musicapp.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.data.model.Playlist
import com.sf.musicapp.data.model.Track

object DiffCallBack {
    val track= object : DiffUtil.ItemCallback<Track>(){
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
    val playlist = object: DiffUtil.ItemCallback<Playlist>(){
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
    val artist = object : DiffUtil.ItemCallback<Artist>(){
        override fun areItemsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    val album = object: DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(
            oldItem: Album,
            newItem: Album
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Album,
            newItem: Album
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

}