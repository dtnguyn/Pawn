package com.nguyen.pawn.repo

import android.util.Log
import com.nguyen.pawn.api.model.*
import com.nguyen.pawn.model.User
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class AuthRepository
    @Inject constructor(
        private val apiClient: HttpClient
    )
{

    suspend fun register(email: String, username: String, password: String, nativeLanguage: String): Boolean {
        val response: Boolean = apiClient.post("http://192.168.0.235:4000/auth/register") {
            contentType(ContentType.Application.Json)
//            body = RegisterRequestBody("test@test.com", "123123", "adron2", "vie")
            body = RegisterRequestBody(email, password, username, nativeLanguage)
        }

        println("register response: $response")

        return response

    }


    suspend fun login(emailOrUsername: String, password: String): LoginResponse? {

        return try {
            apiClient.post("http://192.168.0.235:4000/auth/login") {
                contentType(ContentType.Application.Json)
                body = LoginRequestBody(emailOrUsername, password)
            }
        } catch (error: ClientRequestException) {
            Log.d("Auth", "error: ${error.message}")
            null
        }


    }

    suspend fun logout(refreshToken: String): Boolean {
        return apiClient.delete("http://192.168.0.235:4000/auth/logout") {
            contentType(ContentType.Application.Json)
            body = LogoutRequestBody(refreshToken)
        }
    }

    suspend fun checkAuthStatus(accessToken: String?): User? {

        return apiClient.get("http://192.168.0.235:4000/auth/") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }
    }

    suspend fun  refreshAccessToken(refreshToken: String?): String? {
        val response: LoginResponse? = apiClient.post("http://192.168.0.235:4000/auth/token") {
            contentType(ContentType.Application.Json)
            body = RefreshTokenRequestBody(token = refreshToken)
        }

        return response?.accessToken
    }


}