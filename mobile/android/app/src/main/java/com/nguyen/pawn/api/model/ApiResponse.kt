package com.nguyen.pawn.api.model

data class ApiResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T
)