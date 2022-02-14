package com.nguyen.polyglot.api.model

data class LoginRequestBody(
    val usernameOrEmail: String,
    val password: String,
)


data class LogoutRequestBody(
    val refreshToken: String
)

data class RefreshTokenRequestBody(
    val token: String?
)

data class RegisterRequestBody (
    val email: String,
    val password: String,
    val username: String,
    val nativeLanguage: String,
)

data class UpdateUserBody (
    val username: String,
    val email: String,
    val nativeLanguageId: String,
    val appLanguageId: String,
    val avatar: String?,
    val isPremium: Boolean,
    val dailyWordCount: Int,
    val notificationEnabled: Boolean,
    val dailyWordTopic: String,
    val feedTopics: String,
)