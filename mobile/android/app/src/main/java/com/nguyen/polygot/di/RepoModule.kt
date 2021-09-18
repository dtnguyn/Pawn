package com.nguyen.polygot.di

//import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.polygot.db.PawnDatabase
import com.nguyen.polygot.repo.AuthRepository
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
    fun provideWordRepository(client: HttpClient, database: PawnDatabase): WordRepository {
        return WordRepository(client, database)
    }

    @Singleton
    @Provides
    fun provideLanguageRepository(client: HttpClient, database: PawnDatabase): LanguageRepository {
        return LanguageRepository(client, database)
    }
}