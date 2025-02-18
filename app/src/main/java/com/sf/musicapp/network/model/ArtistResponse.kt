package com.sf.musicapp.network.model

import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.model.Artist
import com.sf.musicapp.utils.Limits
import com.sf.musicapp.utils.truncate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistResponse(
    val headers: Headers,
    val results: List<ArtistMetadata>
)

@Serializable
data class ArtistMetadata(
    val id:String,
    val name:String,
    val website:String,
    @SerialName("joindate") val joinDate:String,
    val image:String,
    @SerialName("shorturl") val shortUrl:String,
    @SerialName("shareurl") val shareUrl:String
)
fun ArtistMetadata.toArtist() =
    Artist(
        id,
        name,
        DateConverter.fromString(joinDate),
        image
    )
