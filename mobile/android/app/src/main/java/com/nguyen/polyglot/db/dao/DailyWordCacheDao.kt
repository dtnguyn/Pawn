package com.nguyen.polyglot.db.dao

import androidx.room.*
import com.nguyen.polyglot.db.entity.DailyWordCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyWordCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(dailyWord: DailyWordCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(dailyWords: List<DailyWordCacheEntity>)

    @Update(entity = DailyWordCacheEntity::class)
    suspend fun updateDailyWord(dailyWord: DailyWordCacheEntity)

    @Query("SELECT * FROM dailyWords WHERE createdDate = :date AND language = :language")
    fun getMany(date: String, language: String): Flow<List<DailyWordCacheEntity>>

    @Query("SELECT * FROM dailyWords WHERE language = :language AND value = :wordValue")
    suspend fun getWordByValue(language: String, wordValue: String): List<DailyWordCacheEntity>

    @Query("DELETE FROM dailyWords WHERE language = :language")
    suspend fun clearAll(language: String)

}