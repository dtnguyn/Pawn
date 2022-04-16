package com.moderndev.polyglot.model

data class ReviewQuestion(
    val word: Word,
    val question: String,
    val answerOptions: List<String>,
    val correctAnswer: String,
    val userAnswer: String?,
    val type: String,
)
