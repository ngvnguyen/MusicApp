package com.sf.musicapp.utils

object Jamendo {
    const val CLIENT_ID ="f89e93af"
    const val JSON = "json"

    enum class OrderBy(val code:String){
        Name("name"),
        Id("id"),
        JoinDate("joindate"),
        PopularityTotal("popularity_total"),
        PopularityMonth("popularity_month"),
        PopularityWeek("popularity_week")
    }

    fun getShareAlbumUrl(albumId:String) = "https://www.jamendo.com/list/a$albumId"
    fun getShareArtistUrl(artistId:String) = "https://www.jamendo.com/artist/$artistId"
    fun getSharePlaylistUrl(playlistId:String) = "https://www.jamendo.com/list/p$playlistId"
    fun getTrackImageUrl(trackId:String,albumId: String)
        = "https://usercontent.jamendo.com?type=album&id=$albumId&width=300&trackid=$trackId"
}