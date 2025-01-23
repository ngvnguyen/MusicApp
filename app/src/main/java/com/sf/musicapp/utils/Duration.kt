package com.sf.musicapp.utils

fun Long.toDuration():String{
    var s = this.toInt()
    val m = (s/60).toInt()
    s = s-m*60
    var result = "$m:$s"
    if (result[1]==':') result= "0$result"
    if (result.length<5) result = result.substring(0,3)+'0'+result.substring(3)
    return result
}