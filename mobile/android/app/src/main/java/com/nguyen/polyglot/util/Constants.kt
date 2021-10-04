package com.nguyen.polyglot.util

import com.nguyen.polyglot.model.Language

object Constants {
    val supportedLanguages = listOf(
        Language("en_US", "English"),
        Language("es", "Spanish"),
        Language("fr", "French"),
        Language("de", "German"),
    )

    val allLanguages = listOf("vie", "eng")

    const val apiURL = "http://192.168.0.169:4000"

}