package com.nguyen.polygot.api.model

data class ApiResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T
)
