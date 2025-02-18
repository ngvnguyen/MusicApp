package com.sf.musicapp.network.model

import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.model.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class PlaylistTrackResponse(
    val headers: Headers,
    val results: List<PlaylistTrackResult>
)

@Serializable
data class PlaylistTrackResult(
    val id: String,
    val name:String,
    @SerialName("creationdate") val creationDate: String,
    @SerialName("user_name") val userName:String,
    @SerialName("user_id") val userId:String,
    val zip:String,
    val tracks: List<PlaylistTrack>
)

@Serializable
data class PlaylistTrack(
    val id:String,
    val name:String,
    @SerialName("album_id") val albumId:String,
    @SerialName("artist_id") val artistId:String,
    val duration:String,
    @SerialName("artist_name") val artistName:String,
    @SerialName("playlistadddate") val playlistAddDate:String,
    val position:String,
    @SerialName("license_ccurl") val licenseCcUrl:String,
    @SerialName("album_image") val albumImage:String,
    val image:String,
    val audio:String,
    @SerialName("audiodownload") val audioDownload:String,
    @SerialName("audiodownload_allowed") val audioDownloadAllowed:Boolean
)

fun PlaylistTrackResult.toListTrack():List<Track>{
    val result = mutableListOf<Track>()
    tracks.forEach {
        val track = Track(
            id = it.id,
            name = it.name,
            artistName = it.artistName,
            duration = it.duration.toInt(),
            artistId = it.artistId,
            albumId = it.albumId,
            releaseDate = DateConverter.fromString(creationDate),
            albumImage = it.albumImage,
            audioDownload = it.audioDownload,
            allowDownload = it.audioDownloadAllowed,
            image = it.image
        )
        result.add(track)
    }
    return result
}
