package com.nguyen.polyglot.model

data class WordDetail (
    val value: String,
    val language: String,
    val topics: String,
    val definitions: List<Definition>,
    val pronunciations: List<Pronunciation>,
)