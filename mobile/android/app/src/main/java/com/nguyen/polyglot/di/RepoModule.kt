package com.nguyen.polyglot.di

import com.nguyen.polyglot.db.PolyglotDatabase
import com.nguyen.polyglot.repo.*
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

    @Singleton
    @Provides
    fun provideWordReviewRepo(database: PolyglotDatabase): WordReviewRepository {
        return WordReviewRepository(database)
    }

    @Singleton
    @Provides
    fun provideUserRepo(client: HttpClient, database: PolyglotDatabase): UserRepository {
        return UserRepository(client, database)
    }

    @Singleton
    @Provides
    fun providePurchaseRepo(client: HttpClient, database: PolyglotDatabase): PurchaseRepository {
        return PurchaseRepository(client, database)
    }
}