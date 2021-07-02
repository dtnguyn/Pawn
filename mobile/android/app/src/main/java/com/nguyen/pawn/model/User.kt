package com.nguyen.pawn.model

import java.util.*

data class User(
    val id: String,
    val oauthId: String?,
    val username: String,
    val email: String,
    val avatar: String?,
    val dailyWordCount: Int,
    val learningLanguages: List<Language>,
    val notificationEnabled: Boolean,
    val nativeLanguageId: String,
    val createdAt: Date,
    val updatedAt: Date
)
