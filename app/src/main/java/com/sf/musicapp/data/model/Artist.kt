package com.sf.musicapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "artist")
data class Artist(
    @PrimaryKey
    val id:String,
    val name:String,
    val joinDate:Date,
    val imageUrl:String
)
