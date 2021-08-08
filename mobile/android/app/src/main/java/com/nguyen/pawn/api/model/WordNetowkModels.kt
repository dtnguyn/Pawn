package com.nguyen.pawn.api.model

import com.nguyen.pawn.model.Language

data class GetDailyWordsRequestBody(
    val dailyWordCount: Int,
    val language: String
)

data class ToggleSavedWordRequestBody(
    val word: String,
    val language: String,
)
