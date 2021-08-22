package com.nguyen.pawn.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nguyen.pawn.db.entity.SavedWordCacheEntity
import com.nguyen.pawn.db.entity.WordDetailCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDetailCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(wordDetail: WordDetailCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(wordDetails: List<WordDetailCacheEntity>)

    @Query("SELECT * FROM wordDetails WHERE value = :value AND language = :language")
    fun getOne(value: String, language: String): Flow<WordDetailCacheEntity?>

    @Query("DELETE FROM wordDetails WHERE value = :value AND language = :language")
    suspend fun clear(value: String, language: String)
}