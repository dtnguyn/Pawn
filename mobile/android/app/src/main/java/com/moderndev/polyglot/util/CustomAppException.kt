package com.moderndev.polyglot.util

data class CustomAppException(override val message: String): Exception(message)
