package com.nguyen.polyglot.model

data class AuthStatus (
    val token: Token,
    val user: User?
)