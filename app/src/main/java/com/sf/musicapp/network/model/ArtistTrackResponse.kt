package com.sf.musicapp.network.model

import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.model.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistTrackResponse(
    val headers: Headers,
    val results: List<ArtistTrackResult>
)
@Serializable
data class ArtistTrackResult(
    val id: String,
    val name: String,
    val website: String,
    @SerialName("joindate") val joinDate: String,
    val image:String,
    val tracks: List<ArtistTrack>
)

@Serializable
data class ArtistTrack(
    @SerialName("album_id") val albumId:String,
    @SerialName("album_name") val albumName:String,
    val id:String,
    val name:String,
    val duration:String,
    @SerialName("releasedate") val releaseDate:String,
    @SerialName("license_ccurl") val licenseCcurl:String,
    @SerialName("album_image") val albumImage:String,
    val image:String,
    val audio:String,
    @SerialName("audiodownload") val audioDownload:String,
    @SerialName("audiodownload_allowed") val audioDownloadAllowed: Boolean
)

fun ArtistTrackResult.toListTrack():List<Track>{
    val result: MutableList<Track> = mutableListOf()
    tracks.forEach {
        val track = Track(
            id = it.id,
            name = it.name,
            artistName = name,
            duration = it.duration.toInt(),
            artistId = id,
            albumId = it.albumId,
            releaseDate = DateConverter.fromString(it.releaseDate),
            albumImage = it.albumImage,
            audioDownload = it.audioDownload,
            allowDownload = it.audioDownloadAllowed,
            image = it.image
        )
        result.add(track)
    }

    return result
}