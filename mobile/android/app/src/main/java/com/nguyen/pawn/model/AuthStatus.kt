package com.nguyen.pawn.model

data class AuthStatus (
    val token: Token,
    val user: User?
)