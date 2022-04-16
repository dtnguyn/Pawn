package com.moderndev.polyglot.di

import android.content.Context
import androidx.room.Room
import com.moderndev.polyglot.db.PolyglotDatabase
import com.moderndev.polyglot.db.PolyglotDatabase.Companion.DATABASE_NAME
import com.moderndev.polyglot.db.dao.*
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
    fun providePawnCacheDb(@ApplicationContext context: Context): PolyglotDatabase {
        return Room
            .databaseBuilder(
                context,
                PolyglotDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }



    @Singleton
    @Provides
    fun provideLanguageCacheDao(database: PolyglotDatabase): LanguageCacheDao {
        return database.languageDao()
    }

    @Singleton
    @Provides
    fun provideDailyWordCacheDao(database: PolyglotDatabase): DailyWordCacheDao {
        return database.dailyWordDao()
    }

    @Singleton
    @Provides
    fun provideSavedWordCacheDao(database: PolyglotDatabase): SavedWordCacheDao {
        return database.savedWordDao()
    }

    @Singleton
    @Provides
    fun provideWordDetailCacheDao(database: PolyglotDatabase): WordDetailCacheDao {
        return database.wordDetailDao()
    }

//    @Singleton
//    @Provides
//    fun provideFeedCacheDao(database: PolygotDatabase): FeedCacheDao {
//        return database.feedDao()
//    }
}