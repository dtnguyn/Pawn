package com.nguyen.polygot.di

//import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.polygot.db.PolygotDatabase
import com.nguyen.polygot.repo.AuthRepository
import com.nguyen.polygot.repo.FeedRepository
import com.nguyen.polygot.repo.LanguageRepository
import com.nguyen.polygot.repo.WordRepository
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
    fun provideWordRepository(client: HttpClient, database: PolygotDatabase): WordRepository {
        return WordRepository(client, database)
    }

    @Singleton
    @Provides
    fun provideLanguageRepository(client: HttpClient, database: PolygotDatabase): LanguageRepository {
        return LanguageRepository(client, database)
    }

    @Singleton
    @Provides
    fun provideFeedRepository(client: HttpClient, database: PolygotDatabase): FeedRepository {
        return FeedRepository(client, database)
    }
}