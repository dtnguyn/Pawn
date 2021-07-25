package com.nguyen.pawn.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nguyen.pawn.db.dao.DailyWordCacheDao
import com.nguyen.pawn.db.dao.LanguageCacheDao
import com.nguyen.pawn.db.entity.DailyWordCacheEntity
import com.nguyen.pawn.db.entity.LanguageCacheEntity

@Database(
    entities = [LanguageCacheEntity::class, DailyWordCacheEntity::class],
    version = 2
)
abstract class PawnDatabase: RoomDatabase() {

    abstract fun languageDao(): LanguageCacheDao
    abstract fun dailyWordDao(): DailyWordCacheDao


    companion object {
        const val DATABASE_NAME: String = "pawn_cache_db"
    }

}