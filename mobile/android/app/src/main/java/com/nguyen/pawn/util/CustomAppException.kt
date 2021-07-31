package com.nguyen.pawn.util

data class CustomAppException(override val message: String): Exception(message)
