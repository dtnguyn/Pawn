package com.nguyen.polygot.model

data class Feed(
    val id: String,
    val type: String,
    val title: String,
    val url: String,
    val thumbnail: String?,
    val author: String?,
    val topic: String?,
    val language: String,
    val description: String,
    val publishedDate: String?
)
