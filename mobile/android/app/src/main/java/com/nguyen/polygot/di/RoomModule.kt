package com.nguyen.polygot.di

import android.content.Context
import androidx.room.Room
import com.nguyen.polygot.db.PawnDatabase
import com.nguyen.polygot.db.PawnDatabase.Companion.DATABASE_NAME
import com.nguyen.polygot.db.dao.DailyWordCacheDao
import com.nguyen.polygot.db.dao.LanguageCacheDao
import com.nguyen.polygot.db.dao.SavedWordCacheDao
import com.nguyen.polygot.db.dao.WordDetailCacheDao
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
    fun providePawnCacheDb(@ApplicationContext context: Context): PawnDatabase {
        return Room
            .databaseBuilder(
                context,
                PawnDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }



    @Singleton
    @Provides
    fun provideLanguageCacheDao(database: PawnDatabase): LanguageCacheDao {
        return database.languageDao()
    }

    @Singleton
    @Provides
    fun provideDailyWordCacheDao(database: PawnDatabase): DailyWordCacheDao {
        return database.dailyWordDao()
    }

    @Singleton
    @Provides
    fun provideSavedWordCacheDao(database: PawnDatabase): SavedWordCacheDao {
        return database.savedWordDao()
    }

    @Singleton
    @Provides
    fun provideWordDetailCacheDao(database: PawnDatabase): WordDetailCacheDao {
        return database.wordDetailDao()
    }
}