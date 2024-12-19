package com.sf.musicapp.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Headers(
    val status:String,
    val code:Int,
    @SerialName("error_message") val errorMessage:String,
    val warnings:String,
    @SerialName("results_count") val resultsCount:Int
)