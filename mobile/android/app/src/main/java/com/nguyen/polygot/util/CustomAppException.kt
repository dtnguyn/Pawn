package com.nguyen.polygot.util

data class CustomAppException(override val message: String): Exception(message)
