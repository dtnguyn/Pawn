package com.nguyen.pawn.api.model

data class RegisterRequestBody (
    val email: String,
    val password: String,
    val username: String,
    val nativeLanguage: String,
)