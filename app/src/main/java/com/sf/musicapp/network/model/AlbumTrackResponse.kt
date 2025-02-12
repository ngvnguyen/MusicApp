package com.sf.musicapp.network.model

import com.sf.musicapp.data.model.Track
import com.sf.musicapp.utils.Jamendo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumTrackResponse(
    val headers: Headers,
    val results:List<AlbumTrackResult>
)

@Serializable
data class AlbumTrackResult(
    val id:String,
    val name:String,
    @SerialName("releasedate") val releaseDate:String,
    @SerialName("artist_id") val artistId:String,
    @SerialName("artist_name") val artistName:String,
    val image:String,
    val zip:String,
    @SerialName("zip_allowed") val zipAllowed:Boolean,
    val tracks:List<AlbumTrack>
)

@Serializable
data class AlbumTrack(
    val id:String,
    val position:String,
    val name:String,
    val duration:String,
    @SerialName("license_ccurl") val liscenseCcurl:String,
    val audio:String,
    @SerialName("audiodownload") val audioDownload:String,
    @SerialName("audiodownload_allowed") val audioDownloadAllowed: Boolean
)

fun AlbumTrackResult.toListTrack():List<Track>{
    val result: MutableList<Track> = mutableListOf()
    this.tracks.forEach{t->
        val track = Track(
            id = t.id,
            name = t.name,
            artistName = artistName,
            duration = t.duration.toInt(),
            artistId = artistId,
            albumId = id,
            releaseDate = releaseDate,
            albumImage = image,
            audioDownload = t.audioDownload,
            allowDownload = t.audioDownloadAllowed,
            image = Jamendo.getTrackImageUrl(t.id,id)
        )
        result.add(track)
    }
    return result
}