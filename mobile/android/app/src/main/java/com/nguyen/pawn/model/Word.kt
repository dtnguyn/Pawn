package com.nguyen.pawn.model

data class Word (
    val value: String,
    val language: String,
    val mainDefinition: String,
    val pronunciationSymbol: String?,
    val pronunciationAudio: String?,
)