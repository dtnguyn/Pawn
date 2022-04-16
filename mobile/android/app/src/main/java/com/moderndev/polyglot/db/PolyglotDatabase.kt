package com.moderndev.polyglot.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moderndev.polyglot.db.dao.*
import com.moderndev.polyglot.db.entity.*
import com.moderndev.polyglot.db.utils.Converters

@Database(
    entities = [LanguageCacheEntity::class, DailyWordCacheEntity::class, SavedWordCacheEntity::class, WordDetailCacheEntity::class, FeedCacheEntity::class],
    version = 6
)
@TypeConverters(Converters::class)
abstract class PolyglotDatabase: RoomDatabase() {

    abstract fun languageDao(): LanguageCacheDao
    abstract fun dailyWordDao(): DailyWordCacheDao
    abstract fun savedWordDao(): SavedWordCacheDao
    abstract fun wordDetailDao(): WordDetailCacheDao
    abstract fun feedDao(): FeedCacheDao



    companion object {
        const val DATABASE_NAME: String = "polyglot_cache_db"
    }

}