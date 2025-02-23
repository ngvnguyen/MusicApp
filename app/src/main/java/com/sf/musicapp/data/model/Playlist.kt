package com.sf.musicapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey
    val id:String,
    val name:String,
    val creationDate: Date,
    val userId:String,
    val userName:String
)