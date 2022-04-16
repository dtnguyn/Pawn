package com.moderndev.polyglot.model

data class SubtitlePart(
    val start: Float,
    val dur: Float,
    val end: Float,
    val text: String?,
    val translatedText: String?,
    val lang: String,
    val translatedLang: String,
)
