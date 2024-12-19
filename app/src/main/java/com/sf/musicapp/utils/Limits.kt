package com.sf.musicapp.utils

object Limits {
    const val ARTIST_CHAR_LIMIT = 12
    const val TRACK_CHAR_LIMIT = 26
    const val PAGE_SIZE = 10
}

fun String.truncate(limit:Int)= if (this.length>limit) this.take(limit-2)+"..."
    else this