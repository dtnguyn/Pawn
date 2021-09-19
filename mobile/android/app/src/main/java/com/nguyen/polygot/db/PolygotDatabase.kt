package com.nguyen.polygot.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nguyen.polygot.db.dao.*
import com.nguyen.polygot.db.entity.*
import com.nguyen.polygot.db.utils.Converters

@Database(
    entities = [LanguageCacheEntity::class, DailyWordCacheEntity::class, SavedWordCacheEntity::class, WordDetailCacheEntity::class, FeedCacheEntity::class],
    version = 5
)
@TypeConverters(Converters::class)
abstract class PolygotDatabase: RoomDatabase() {

    abstract fun languageDao(): LanguageCacheDao
    abstract fun dailyWordDao(): DailyWordCacheDao
    abstract fun savedWordDao(): SavedWordCacheDao
    abstract fun wordDetailDao(): WordDetailCacheDao
    abstract fun feedDao(): FeedCacheDao



    companion object {
        const val DATABASE_NAME: String = "pawn_cache_db"
    }

}