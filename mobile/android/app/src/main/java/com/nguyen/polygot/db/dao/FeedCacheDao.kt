package com.nguyen.polygot.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nguyen.polygot.db.entity.DailyWordCacheEntity
import com.nguyen.polygot.db.entity.FeedCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(feed: FeedCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(feeds: List<FeedCacheEntity>)

    @Query("SELECT * FROM feeds WHERE language = :language")
    fun getMany(language: String): Flow<List<FeedCacheEntity>>

    @Query("DELETE FROM feeds WHERE language = :language")
    suspend fun clearAll(language: String)


}