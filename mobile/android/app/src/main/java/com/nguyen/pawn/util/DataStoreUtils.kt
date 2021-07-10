package com.nguyen.pawn.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DataStoreUtils{

    private val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
    private val PICKED_LANGUAGES_KEY = stringPreferencesKey("PICKED_LANGUAGES")

    private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    suspend fun saveAccessTokenToAuthDataStore(context: Context, accessToken: String?){
        context.authDataStore.edit { auth ->
            auth[ACCESS_TOKEN_KEY] = accessToken ?: ""
        }
    }

    suspend fun getAccessTokenFromDataStore(context: Context): String?{
        val token =  context.authDataStore.data.map {preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }.first()

        return if(token == "") null
        else token
    }

    suspend fun saveRefreshTokenToAuthDataStore(context: Context, refreshToken: String?){
        context.authDataStore.edit { auth ->
            auth[REFRESH_TOKEN_KEY] = refreshToken ?: ""
        }
    }

    suspend fun getRefreshTokenFromDataStore(context: Context): String?{
        val token =  context.authDataStore.data.map {preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }.first()

        return if(token == "") null
        else token
    }

}