package com.sf.musicapp.network.model

import com.sf.musicapp.data.model.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackResponse(
    val headers: Headers,
    val results: List<TrackMetadata>
)
@Serializable
data class TrackMetadata(
    val id:String,
    val name:String,
    val duration:Int,
    @SerialName("artist_id") val artistId:String,
    @SerialName("artist_name") val artistName:String,
    @SerialName("artist_idstr") val artistIdStr:String,
    @SerialName("album_name") val albumName:String,
    @SerialName("album_id") val albumId:String,
    @SerialName("license_ccurl") val licenseCcurl:String,
    val position:Int,
    @SerialName("releasedate") val releaseDate:String,
    @SerialName("album_image") val albumImage:String,
    val audio:String,
    @SerialName("audiodownload") val audioDownload:String,
    @SerialName("prourl") val proUrl:String,
    @SerialName("shorturl") val shortUrl:String,
    @SerialName("shareurl") val shareUrl:String,
    val image:String,
    @SerialName("audiodownload_allowed") val allowDownload:Boolean
)
fun TrackMetadata.toTrack() = Track(
    id = id,
    name = name,
    artistName= artistName,
    duration = duration,
    artistId = artistId,
    albumId = albumId,
    releaseDate = releaseDate,
    albumImage = albumImage,
    audioDownload = audioDownload,
    allowDownload = allowDownload
)