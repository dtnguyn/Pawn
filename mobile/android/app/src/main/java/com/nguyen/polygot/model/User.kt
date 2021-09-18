package com.nguyen.polygot.model

data class User(
    val id: String,
    val oauthId: String?,
    val username: String,
    val email: String,
    val avatar: String?,
    val dailyWordCount: Int,
    val notificationEnabled: Boolean,
    val nativeLanguageId: String,
)
