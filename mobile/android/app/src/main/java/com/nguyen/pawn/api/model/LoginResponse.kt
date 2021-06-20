package com.nguyen.pawn.api.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)