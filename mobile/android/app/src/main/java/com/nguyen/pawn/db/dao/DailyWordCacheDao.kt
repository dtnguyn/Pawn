package com.nguyen.pawn.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nguyen.pawn.db.entity.DailyWordCacheEntity
import com.nguyen.pawn.db.entity.LanguageCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyWordCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(dailyWord: DailyWordCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(dailyWords: List<DailyWordCacheEntity>)

    @Query("SELECT * FROM dailyWords WHERE createdDate = :date AND language = :language")
    fun getMany(date: String, language: String): Flow<List<DailyWordCacheEntity>>

    @Query("DELETE FROM dailyWords WHERE language = :language")
    suspend fun clearAll(language: String)

}