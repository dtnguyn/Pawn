package com.nguyen.polyglot.util

data class CustomAppException(override val message: String): Exception(message)
