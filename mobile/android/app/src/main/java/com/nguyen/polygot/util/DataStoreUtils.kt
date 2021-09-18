package com.nguyen.polygot.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nguyen.polygot.db.datastore.tokenDataStore
import com.nguyen.polygot.db.datastore.userDataStore
import com.nguyen.polygot.model.Token
import com.nguyen.polygot.model.User
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
        val token =  context.tokenDataStore.data.map {tokenDs ->
            tokenDs.accessToken
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
        val token = context.tokenDataStore.data.map { tokenDs ->
            tokenDs.refreshToken
        }.first()

        return if(token == "") null
        else token
    }

    suspend fun getUserFromDataStore(context: Context): User?{
        return context.userDataStore.data.map { user ->
            if(user.id.isEmpty()) null
            else {
                User(
                    id = user.id,
                    oauthId = if (user.oauthId.isBlank()) null else user.oauthId,
                    username = user.username,
                    email = user.email,
                    avatar = if (user.avatar.isBlank()) null else user.avatar,
                    dailyWordCount = user.dailyWordCount,
                    nativeLanguageId = user.nativeLanguageId,
                    notificationEnabled = user.notificationEnabled
                )
            }
        }.first()
    }

    suspend fun saveTokenToDataStore(context: Context, token: Token){
        context.tokenDataStore.updateData { currentToken ->
            currentToken.toBuilder()
                .setAccessToken(token.accessToken)
                .setRefreshToken(token.refreshToken)
                .build()
        }
    }

    suspend fun saveUserToDataStore(context: Context, user: User){
        val oauthId = user.oauthId ?: ""
        val avatar = user.avatar ?: ""
        context.userDataStore.updateData { currentToken ->
            currentToken.toBuilder()
                .setId(user.id)
                .setOauthId(oauthId)
                .setUsername(user.username)
                .setEmail(user.email)
                .setAvatar(avatar)
                .setDailyWordCount(user.dailyWordCount)
                .setNotificationEnabled(user.notificationEnabled)
                .setNativeLanguageId(user.nativeLanguageId)
                .build()
        }
    }

}