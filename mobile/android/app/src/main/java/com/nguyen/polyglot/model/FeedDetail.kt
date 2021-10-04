package com.nguyen.polyglot.model

data class FeedDetail<T>(
    val id: String,
    val type: String,
    val content: T
)