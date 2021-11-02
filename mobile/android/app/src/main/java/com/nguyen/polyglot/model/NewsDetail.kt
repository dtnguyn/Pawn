package com.nguyen.polyglot.model

data class NewsDetail(
    val value: String,
    val images: List<String>,
    val source: String?,
    val publishedDate: String?
)
