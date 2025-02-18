package com.sf.musicapp.network.model

import com.sf.musicapp.data.converter.DateConverter
import com.sf.musicapp.data.model.Album
import com.sf.musicapp.utils.Jamendo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ArtistAlbumResponse(
    val headers: Headers,
    val results: List<ArtistAlbumResult>
)

@Serializable
data class ArtistAlbumResult(
    val id:String,
    val name:String,
    val website:String,
    @SerialName("joindate") val joiner: String,
    val image:String,
    val albums:List<ArtistAlbum>
)

@Serializable
data class ArtistAlbum(
    val id: String,
    val name: String,
    @SerialName("releasedate") val releaseDate: String,
    val image: String
)

fun ArtistAlbumResult.toListAlbum(): List<Album> {
    val result = mutableListOf<Album>()
    albums.forEach {
        val album = Album(
            id = it.id,
            name = it.name,
            releaseDate = DateConverter.fromString(it.releaseDate),
            artistId = id,
            artistName = name,
            image = it.image,
            shareUrl = Jamendo.getShareAlbumUrl(it.id)
        )
        result.add(album)
    }
    return result
}