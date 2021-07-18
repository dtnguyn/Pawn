package com.nguyen.pawn.repo

import android.util.Log
import com.nguyen.pawn.api.model.*
import com.nguyen.pawn.model.Token
import com.nguyen.pawn.model.User
import com.nguyen.pawn.util.Constants
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.ConnectException
import javax.inject.Inject

class AuthRepository
    @Inject constructor(
        private val apiClient: HttpClient
    )
{
    companion object{
        private const val TAG = "AuthRepo"
    }

    suspend fun register(email: String, username: String, password: String, nativeLanguage: String): Token? {
        return try {
            val response: ApiResponse<Void> = apiClient.post("${Constants.apiURL}/auth/register") {
                contentType(ContentType.Application.Json)
                body = RegisterRequestBody(email, password, username, nativeLanguage)
            }
            if(response.status) login(email, password)
            else null
        } catch (error: ClientRequestException) {
            Log.d(TAG, "register error: ${error.message}")
            null
        } catch (error: ConnectException) {
            Log.d(TAG, "register error: ${error.message}")
            null
        }
    }


    suspend fun login(emailOrUsername: String, password: String): Token? {
        return try {
            val response: ApiResponse<Token?> = apiClient.post("${Constants.apiURL}/auth/login") {
                contentType(ContentType.Application.Json)
                body = LoginRequestBody(emailOrUsername, password)
            }
            response.data
        } catch (error: ClientRequestException) {
            Log.d(TAG, "login error: ${error.message}")
            null
        } catch (error: ConnectException) {
            Log.d(TAG, "login error: ${error.message}")
            null
        }
    }

    suspend fun logout(refreshToken: String): Boolean {
        return try {
            val response: ApiResponse<Void> = apiClient.delete("${Constants.apiURL}/auth/logout") {
                contentType(ContentType.Application.Json)
                body = LogoutRequestBody(refreshToken)
            }
            response.status
        } catch (error: ClientRequestException) {
            Log.d(TAG, "logout error: ${error.message}")
            false
        } catch (error: ConnectException) {
            Log.d(TAG, "logout error: ${error.message}")
            false
        }
    }

    suspend fun checkAuthStatus(accessToken: String?): User? {
        return try {
            val response: ApiResponse<User?> = apiClient.get("${Constants.apiURL}/auth/") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
            response.data
        } catch (error: ClientRequestException) {
            Log.d(TAG, "checkAuthStatus error: ${error.message}")
            null
        } catch (error: ConnectException) {
            Log.d(TAG, "checkAuthStatus error: ${error.message}")
            null
        }
    }

    suspend fun  refreshAccessToken(refreshToken: String?): String? {
        return try {
            val response: ApiResponse<Token?> = apiClient.post("${Constants.apiURL}/auth/token") {
                contentType(ContentType.Application.Json)
                body = RefreshTokenRequestBody(token = refreshToken)
            }
            response.data?.accessToken
        } catch (error: ClientRequestException) {
            Log.d(TAG, "refreshAccessToken error: ${error.message}")
            null
        } catch (error: ConnectException) {
            Log.d(TAG, "refreshAccessToken error: ${error.message}")
            null
        }
    }


}