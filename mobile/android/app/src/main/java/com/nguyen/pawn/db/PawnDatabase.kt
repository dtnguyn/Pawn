package com.nguyen.pawn.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nguyen.pawn.db.dao.DailyWordCacheDao
import com.nguyen.pawn.db.dao.LanguageCacheDao
import com.nguyen.pawn.db.dao.SavedWordCacheDao
import com.nguyen.pawn.db.entity.DailyWordCacheEntity
import com.nguyen.pawn.db.entity.LanguageCacheEntity
import com.nguyen.pawn.db.entity.SavedWordCacheEntity

@Database(
    entities = [LanguageCacheEntity::class, DailyWordCacheEntity::class, SavedWordCacheEntity::class],
    version = 3
)
abstract class PawnDatabase: RoomDatabase() {

    abstract fun languageDao(): LanguageCacheDao
    abstract fun dailyWordDao(): DailyWordCacheDao
    abstract fun savedWordDao(): SavedWordCacheDao


    companion object {
        const val DATABASE_NAME: String = "pawn_cache_db"
    }

}