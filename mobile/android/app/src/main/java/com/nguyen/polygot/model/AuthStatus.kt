package com.nguyen.polygot.model

data class AuthStatus (
    val token: Token,
    val user: User?
)