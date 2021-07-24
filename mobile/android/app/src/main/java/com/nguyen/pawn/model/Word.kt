package com.nguyen.pawn.model

data class Word (
    val value: String,
    val language: String,
    val definitions: List<Definition>,
    val pronunciations: List<Pronunciation>,
)