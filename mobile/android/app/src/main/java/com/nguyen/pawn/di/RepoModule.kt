package com.nguyen.pawn.di

//import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.pawn.repo.AuthRepository
import com.nguyen.pawn.repo.LanguageRepository
import com.nguyen.pawn.repo.WordRepository
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
    fun provideWordRepository(client: HttpClient): WordRepository {
        return WordRepository(client)
    }

    @Singleton
    @Provides
    fun provideLanguageRepository(client: HttpClient, database: PawnDatabase): LanguageRepository {
        return LanguageRepository(client, database)
    }
}