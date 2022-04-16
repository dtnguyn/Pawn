package com.moderndev.polyglot.db.dao

import androidx.room.*
import com.moderndev.polyglot.db.entity.FeedCacheEntity
//import com.nguyen.polygot.db.entity.FeedCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(feed: FeedCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(feeds: List<FeedCacheEntity>)

    @Query("SELECT * FROM feeds WHERE language = :language")
    fun getMany(language: String): Flow<List<FeedCacheEntity>>

    @Query("SELECT * FROM feeds WHERE language = :language AND topic LIKE :topics")
    fun getManyWithFilter(language: String, topics: String): Flow<List<FeedCacheEntity>>



    @Query("SELECT * FROM feeds WHERE language = :language AND cacheDate = :date")
    fun getManyWithDate(language: String, date: String): Flow<List<FeedCacheEntity>>

    @Query("DELETE FROM feeds WHERE language = :language")
    suspend fun clearAll(language: String)


}