package com.nguyen.polygot.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nguyen.polygot.model.Definition
import com.nguyen.polygot.model.Pronunciation

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