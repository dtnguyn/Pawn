package com.moderndev.polyglot.model

data class LanguageReport(
    val languageId: String,
    val savedWordCount: Int,
    val wordTopicReports: List<WordTopicReport>
)