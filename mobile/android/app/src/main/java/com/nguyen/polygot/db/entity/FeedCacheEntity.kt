package com.nguyen.polygot.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "feeds")
class FeedCacheEntity(

    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "thumbnail")
    val thumbnail: String?,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "topic")
    val topic: String?,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "description")
    val publishedDate: String?,

    @ColumnInfo(name = "description")
    val cacheDate: String,

    )