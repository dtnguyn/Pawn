package com.nguyen.polygot.model

data class WordDetail (
    val value: String,
    val language: String,
    val definitions: List<Definition>,
    val pronunciations: List<Pronunciation>,
)