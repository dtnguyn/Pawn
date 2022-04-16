package com.moderndev.polyglot.model

data class FeedDetail<T>(
    val id: String,
    val type: String,
    val thumbnail: String,
    val title: String,
    val content: T
)