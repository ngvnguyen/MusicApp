package com.sf.musicapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "track")
data class Track(
    @PrimaryKey
    val id:String,
    val name:String,
    val artistName:String,
    val duration:Int,
    val artistId:String,
    val albumId:String,
    val releaseDate:Date,
    val albumImage:String,
    val audioDownload:String,
    val allowDownload:Boolean,
    val image:String
)