package com.sf.musicapp.network.model

import com.sf.musicapp.data.model.Playlist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistResponse(
    val headers: Headers,
    val results:List<PlaylistMetadata>
)

@Serializable
data class PlaylistMetadata(
    val id:String,
    val name:String,
    @SerialName("creationdate") val creationDate:String,
    @SerialName("user_id") val userId:String,
    @SerialName("user_name") val userName:String,
    val zip:String,
    @SerialName("shorturl") val shortUrl:String,
    @SerialName("shareurl") val shareUrl:String
)

fun PlaylistMetadata.toPlaylist()=
    Playlist(
        id=id,
        name = name,
        creationDate = creationDate,
        userId = userId,
        userName = userName
    )