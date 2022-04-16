package com.moderndev.polyglot.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "dailyWords")
class DailyWordCacheEntity (

    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "value")
    val value: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = "topics")
    val topics: String,

    @ColumnInfo(name = "display")
    var display: Boolean,

    @ColumnInfo(name = "pronunciationAudio")
    val pronunciationAudio: String?,

    @ColumnInfo(name = "pronunciationSymbol")
    val pronunciationSymbol: String?,

    @ColumnInfo(name = "mainDefinition")
    val mainDefinition: String,

    @ColumnInfo(name = "createdDate")
    val createdDate: String,
)