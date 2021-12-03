package com.nguyen.polyglot.model

data class LanguageReport(
    val languageId: String,
    val savedWordCount: Int,
    val wordTopicReports: List<WordTopicReport>
)