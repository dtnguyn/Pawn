package com.nguyen.polyglot.di

import com.nguyen.polyglot.db.PolyglotDatabase
import com.nguyen.polyglot.repo.AuthRepository
import com.nguyen.polyglot.repo.LanguageRepository
import com.nguyen.polyglot.repo.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Singleton
    @Provides
    fun provideAuthRepository(client: HttpClient): AuthRepository {
        return AuthRepository(client)
    }

    @Singleton
    @Provides
    fun provideWordRepository(client: HttpClient, database: PolyglotDatabase): WordRepository {
        return WordRepository(client, database)
    }

    @Singleton
    @Provides
    fun provideLanguageRepository(client: HttpClient, database: PolyglotDatabase): LanguageRepository {
        return LanguageRepository(client, database)
    }

//    @Singleton
//    @Provides
//    fun provideFeedRepository(client: HttpClient, database: PolygotDatabase): FeedRepository {
//        return FeedRepository(client, database)
//    }
}