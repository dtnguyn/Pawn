package com.nguyen.polyglot.repo

import android.util.Log
import com.nguyen.polyglot.api.model.*
import com.nguyen.polyglot.model.AuthStatus
import com.nguyen.polyglot.model.Token
import com.nguyen.polyglot.model.User
import com.nguyen.polyglot.repo.utils.mainGetNetworkBoundResource
import com.nguyen.polyglot.repo.utils.mainPostNetworkBoundResource
import com.nguyen.polyglot.util.Constants
import com.nguyen.polyglot.util.CustomAppException
import com.nguyen.polyglot.util.UIState
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

    suspend fun checkAuthStatus(accessToken: String?, refreshToken: String?): Flow<UIState<AuthStatus>> {
        var currentUser: User? = null
        var currentToken = Token(accessToken, refreshToken)
        return mainGetNetworkBoundResource(
            query = {
                flow { emit(AuthStatus(currentToken, currentUser)) }
            },
            fetch = {
                val response: ApiResponse<User?> = apiClient.get("${Constants.apiURL}/auth/") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
                Log.d(TAG, "refreshTokenResponse1: $response")
                if(response.status) {
                    if(response.data == null){
                        val refreshTokenResponse: ApiResponse<Token?> = apiClient.post("${Constants.apiURL}/auth/token") {
                            contentType(ContentType.Application.Json)
                            body = RefreshTokenRequestBody(token = refreshToken)
                        }
                        Log.d(TAG, "refreshTokenResponse2: $refreshTokenResponse")
                        if(refreshTokenResponse.status){
                            val retryResponse: ApiResponse<User?> = apiClient.get("${Constants.apiURL}/auth/") {
                                headers {
                                    append(HttpHeaders.Authorization, "Bearer ${refreshTokenResponse.data?.accessToken}")
                                }
                            }
                            Log.d(TAG, "refreshTokenResponse3: $retryResponse")

                            if(retryResponse.status){
                                currentUser = retryResponse.data
                                currentToken = refreshTokenResponse.data!!
                            } else throw CustomAppException(retryResponse.message)
                        } else throw CustomAppException(refreshTokenResponse.message)
                    } else {
                        currentUser = response.data
                    }
                }
                else throw CustomAppException(response.message)
                AuthStatus(currentToken, currentUser)
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

    suspend fun updateUser(
        accessToken: String,
        username: String,
        email: String,
        nativeLanguageId: String,
        avatar: String?,
        dailyWordCount: Int,
        notificationEnabled: Boolean,
        currentAuthStatus: AuthStatus
    ): Flow<UIState<AuthStatus>>{
        return mainPostNetworkBoundResource(
            submit = {
                Log.d("AccountScreen", "Debug Account Screen 4")

                val response: ApiResponse<User?> = apiClient.put("${Constants.apiURL}/auth/user") {
                    contentType(ContentType.Application.Json)
                    body = UpdateUserBody(username, email, nativeLanguageId, avatar, dailyWordCount, notificationEnabled)
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
                Log.d("AccountScreen", "Debug Account Screen 5 ${response}")

                var newUser: User? = null
                if(response.status) newUser = response.data
                else throw CustomAppException(response.message)
                val newAuthStatus = AuthStatus(currentAuthStatus.token, newUser)
                newAuthStatus
            },
            shouldSave = {
                 false
            },
            saveSubmitResult = {}
        )
    }

}