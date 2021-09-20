package com.nguyen.polygot.di

import android.content.Context
import androidx.room.Room
import com.nguyen.polygot.db.PolygotDatabase
import com.nguyen.polygot.db.PolygotDatabase.Companion.DATABASE_NAME
import com.nguyen.polygot.db.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providePawnCacheDb(@ApplicationContext context: Context): PolygotDatabase {
        return Room
            .databaseBuilder(
                context,
                PolygotDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }



    @Singleton
    @Provides
    fun provideLanguageCacheDao(database: PolygotDatabase): LanguageCacheDao {
        return database.languageDao()
    }

    @Singleton
    @Provides
    fun provideDailyWordCacheDao(database: PolygotDatabase): DailyWordCacheDao {
        return database.dailyWordDao()
    }

    @Singleton
    @Provides
    fun provideSavedWordCacheDao(database: PolygotDatabase): SavedWordCacheDao {
        return database.savedWordDao()
    }

    @Singleton
    @Provides
    fun provideWordDetailCacheDao(database: PolygotDatabase): WordDetailCacheDao {
        return database.wordDetailDao()
    }

//    @Singleton
//    @Provides
//    fun provideFeedCacheDao(database: PolygotDatabase): FeedCacheDao {
//        return database.feedDao()
//    }
}