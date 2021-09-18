package com.nguyen.polygot.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nguyen.polygot.db.entity.SavedWordCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedWordCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(savedWord: SavedWordCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(savedWords: List<SavedWordCacheEntity>)

    @Query("SELECT * FROM savedWords WHERE language = :language")
    fun getMany(language: String): Flow<List<SavedWordCacheEntity>>

    @Query("DELETE FROM savedWords WHERE language = :language")
    suspend fun clearAll(language: String)

}