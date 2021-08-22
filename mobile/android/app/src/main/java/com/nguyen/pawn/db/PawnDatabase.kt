package com.nguyen.pawn.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nguyen.pawn.db.dao.DailyWordCacheDao
import com.nguyen.pawn.db.dao.LanguageCacheDao
import com.nguyen.pawn.db.dao.SavedWordCacheDao
import com.nguyen.pawn.db.dao.WordDetailCacheDao
import com.nguyen.pawn.db.entity.DailyWordCacheEntity
import com.nguyen.pawn.db.entity.LanguageCacheEntity
import com.nguyen.pawn.db.entity.SavedWordCacheEntity
import com.nguyen.pawn.db.entity.WordDetailCacheEntity
import com.nguyen.pawn.db.utils.Converters

@Database(
    entities = [LanguageCacheEntity::class, DailyWordCacheEntity::class, SavedWordCacheEntity::class, WordDetailCacheEntity::class],
    version = 4
)
@TypeConverters(Converters::class)
abstract class PawnDatabase: RoomDatabase() {

    abstract fun languageDao(): LanguageCacheDao
    abstract fun dailyWordDao(): DailyWordCacheDao
    abstract fun savedWordDao(): SavedWordCacheDao
    abstract fun wordDetailDao(): WordDetailCacheDao


    companion object {
        const val DATABASE_NAME: String = "pawn_cache_db"
    }

}