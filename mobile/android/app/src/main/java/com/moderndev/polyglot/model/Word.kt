package com.moderndev.polyglot.model

data class Word (
    val value: String,
    val language: String,
    val topics: String,
    val mainDefinition: String,
    val pronunciationSymbol: String?,
    val pronunciationAudio: String?,
)