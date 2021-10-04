package com.nguyen.polyglot.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nguyen.polyglot.model.Definition
import com.nguyen.polyglot.model.Pronunciation

@Entity(tableName = "wordDetails")
class WordDetailCacheEntity (

    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "value")
    val value: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = "definitions")
    val definitions: List<Definition>,

    @ColumnInfo(name = "pronunciations")
    val pronunciations: List<Pronunciation>,
)