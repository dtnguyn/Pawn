package com.nguyen.pawn.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nguyen.pawn.db.entity.DailyWordCacheEntity
import com.nguyen.pawn.db.entity.LanguageCacheEntity

@Dao
interface DailyWordCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(dailyWord: DailyWordCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(dailyWords: List<DailyWordCacheEntity>)

    @Query("SELECT * FROM dailyWords WHERE createdDate = :date AND language = :language")
    suspend fun getMany(date: String, language: String): List<DailyWordCacheEntity>

    @Query("DELETE FROM dailyWords")
    suspend fun clearAll()

}