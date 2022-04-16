package com.moderndev.polyglot.model

data class NewsDetail(
    val value: String,
    val images: List<String>,
    val source: String?,
    val author: String?,
    val publishedDate: String?
)
