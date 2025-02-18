package com.sf.musicapp.data.model

import androidx.room.Dao
import androidx.room.Entity
import java.util.Date

@Entity(tableName = "album")
data class Album(
    val id:String,
    val name:String,
    val releaseDate:Date,
    val artistId:String,
    val artistName:String,
    val image:String,
    val shareUrl:String
)
