package com.nguyen.pawn.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nguyen.pawn.db.entity.LanguageCacheEntity

@Dao
interface LanguageCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(language: LanguageCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(languages: List<LanguageCacheEntity>)

    @Query("SELECT * FROM languages")
    suspend fun getMany(): List<LanguageCacheEntity>

    @Query("DELETE FROM languages")
    suspend fun clearAll()

}