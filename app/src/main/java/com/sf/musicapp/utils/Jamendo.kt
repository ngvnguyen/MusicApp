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
}