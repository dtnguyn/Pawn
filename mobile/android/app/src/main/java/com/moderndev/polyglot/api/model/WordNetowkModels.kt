package com.moderndev.polyglot.api.model

data class GetDailyWordsRequestBody(
    val dailyWordCount: Int,
    val language: String
)

data class ToggleSavedWordRequestBody(
    val word: String,
    val language: String,
)
