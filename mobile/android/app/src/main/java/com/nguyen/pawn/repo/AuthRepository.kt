package com.nguyen.pawn.repo

import android.util.Log
import com.nguyen.pawn.api.model.*
import com.nguyen.pawn.model.Token
import com.nguyen.pawn.model.User
import com.nguyen.pawn.repo.utils.mainGetNetworkBoundResource
import com.nguyen.pawn.util.Constants
import com.nguyen.pawn.util.CustomAppException
import com.nguyen.pawn.util.UIState
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository
@Inject constructor(
    private val apiClient: HttpClient
) {
    companion object {
        private const val TAG = "AuthRepo"
    }

    suspend fun register(
        email: String,
        username: String,
        password: String,
        nativeLanguage: String
    ): Flow<UIState<Token>> {
        var token: Token? = null
        return mainGetNetworkBoundResource(
            query = {
                flow { emit(token) }
            },
            fetch = {
                val response: ApiResponse<Void> =
                    apiClient.post("${Constants.apiURL}/auth/register") {
                        contentType(ContentType.Application.Json)
                        body = RegisterRequestBody(email, password, username, nativeLanguage)
                    }
                if (response.status) {
                    val loginResponse: ApiResponse<Token?> =
                        apiClient.post("${Constants.apiURL}/auth/login") {
                            contentType(ContentType.Application.Json)
                            body = LoginRequestBody(email, password)
                        }
                    if (loginResponse.status) token = loginResponse.data
                    else throw CustomAppException(response.message)

                    token
                } else throw CustomAppException(response.message)

            },
            saveFetchResult = {

            },
            tag = TAG
        )
    }


    suspend fun login(emailOrUsername: String, password: String): Flow<UIState<Token>> {
        var token: Token? = null
        return mainGetNetworkBoundResource(
            query = {
                flow { emit(token) }
            },
            fetch = {
                val loginResponse: ApiResponse<Token?> =
                    apiClient.post("${Constants.apiURL}/auth/login") {
                        contentType(ContentType.Application.Json)
                        body = LoginRequestBody(emailOrUsername, password)
                    }
                Log.d(TAG, "login response $loginResponse")
                if (loginResponse.status) token = loginResponse.data
                else throw CustomAppException(loginResponse.message)
                token
            },
            saveFetchResult = {},
            tag = TAG
        )
    }

    suspend fun logout(refreshToken: String): Flow<UIState<Boolean>> {
        var result: Boolean? = null
        return mainGetNetworkBoundResource(
            query = {
                flow { emit(result) }
            },
            fetch = {
                val response: ApiResponse<Void> =
                    apiClient.delete("${Constants.apiURL}/auth/logout") {
                        contentType(ContentType.Application.Json)
                        body = LogoutRequestBody(refreshToken)
                    }
                if(response.status) result = response.status
                else throw CustomAppException(response.message)
                result
            },
            saveFetchResult = {},
            tag = TAG
        )

    }

    suspend fun checkAuthStatus(accessToken: String?): Flow<UIState<User>> {
        var currentUser: User? = null
        return mainGetNetworkBoundResource(
            query = {
                flow { emit(currentUser) }
            },
            fetch = {
                val response: ApiResponse<User?> = apiClient.get("${Constants.apiURL}/auth/") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
                if(response.status) currentUser = response.data
                else throw CustomAppException(response.message)
                currentUser
            },
            saveFetchResult = {},
            tag = TAG
        )
    }

    suspend fun refreshAccessToken(refreshToken: String?): Flow<UIState<String>> {
        var newAccessToken: String? = null
        return mainGetNetworkBoundResource(
            query = {
                flow { emit(newAccessToken) }
            },
            fetch = {
                val response: ApiResponse<Token?> = apiClient.post("${Constants.apiURL}/auth/token") {
                    contentType(ContentType.Application.Json)
                    body = RefreshTokenRequestBody(token = refreshToken)
                }
                if(response.status) newAccessToken = response.data?.accessToken
                else throw CustomAppException(response.message)
                newAccessToken
            },
            saveFetchResult = {},
            tag = TAG
        )
    }

}