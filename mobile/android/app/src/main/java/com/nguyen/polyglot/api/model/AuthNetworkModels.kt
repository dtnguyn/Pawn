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