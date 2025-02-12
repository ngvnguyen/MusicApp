package com.sf.musicapp.utils

object Limits {
    const val ARTIST_CHAR_LIMIT = 12
    const val ALBUM_CHAR_LIMIT = 12
    const val TRACK_CHAR_LIMIT = 20
    const val PLAYLIST_CHAR_LIMIT = 11
    const val PAGE_SIZE = 15

    const val MAX_ITEM = 8
}

fun String.truncate(limit:Int)= if (this.length>limit) this.take(limit-2)+"..."
    else this