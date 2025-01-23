package com.sf.musicapp.network.model

import com.sf.musicapp.data.model.Album
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumResponse(
    val headers: Headers,
    val results:List<AlbumMetadata>
)

@Serializable
data class AlbumMetadata(
    val id:String,
    val name:String,
    @SerialName("releasedate") val releaseDate:String,
    @SerialName("artist_id") val artistId:String,
    @SerialName("artist_name") val artistName:String,
    val image:String,
    val zip:String,
    @SerialName("shorturl") val shortUrl:String,
    @SerialName("shareurl") val shareUrl:String,
    @SerialName("zip_allowed") val zipAllowed:Boolean
)

fun AlbumMetadata.toAlbum()=
    Album(
        id = id,
        name = name,
        releaseDate = releaseDate,
        artistId = artistId,
        artistName = artistName,
        image = image
    )
