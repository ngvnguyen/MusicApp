package com.sf.musicapp.data.model

data class Track(
    val id:String,
    val name:String,
    val artistName:String,
    val duration:Int,
    val artistId:String,
    val albumId:String,
    val releaseDate:String,
    val albumImage:String,
    val audioDownload:String,
    val allowDownload:Boolean
)