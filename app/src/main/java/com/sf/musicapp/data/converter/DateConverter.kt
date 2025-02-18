package com.sf.musicapp.data.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateConverter {
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun fromTimestamp(value:Long):Date{
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date:Date):Long{
        return date.time
    }

    fun fromString(dateString:String):Date{
        return format.parse(dateString)
    }
    fun fromDate(date:Date):String{
        return format.format(date)
    }
}