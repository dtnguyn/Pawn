package com.nguyen.pawn.api.model

data class LoginRequestBody(
    val usernameOrEmail: String,
    val password: String,
)
