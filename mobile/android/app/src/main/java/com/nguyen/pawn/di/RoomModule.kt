package com.nguyen.pawn.di

import android.content.Context
import androidx.room.Room
import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.pawn.db.PawnDatabase.Companion.DATABASE_NAME
import com.nguyen.pawn.db.dao.LanguageCacheDao
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
            .build()
    }



    @Singleton
    @Provides
    fun provideLanguageCacheDao(database: PawnDatabase): LanguageCacheDao {
        return database.languageDao()
    }
}