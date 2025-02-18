package com.sf.musicapp.data.model

import androidx.room.Entity
import java.util.Date

@Entity(tableName = "playlist")
data class Playlist(
    val id:String,
    val name:String,
    val creationDate: Date,
    val userId:String,
    val userName:String
)