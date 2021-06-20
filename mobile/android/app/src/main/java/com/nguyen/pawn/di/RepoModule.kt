package com.nguyen.pawn.di

import com.nguyen.pawn.repo.AuthRepository
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
}