package com.nguyen.pawn.repo

import com.nguyen.pawn.api.model.LoginRequestBody
import com.nguyen.pawn.api.model.LoginResponse
import com.nguyen.pawn.api.model.RegisterRequestBody
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject

class AuthRepository
    @Inject constructor(
        private val apiClient: HttpClient
    )
{

    suspend fun register(email: String, username: String, password: String, nativeLanguage: String): LoginResponse? {
        val response: Boolean = apiClient.post("http://192.168.0.235:4000/auth/register") {
            contentType(ContentType.Application.Json)
            body = RegisterRequestBody("test@test.com", "123123", "adron2", "vie")
//            body = RegisterRequestBody(email, password, username, nativeLanguage)
        }

        println("register response: $response")

        return if(response) {
            login("test@test.com", "123123")
//            login(email, username)
        } else null

    }


    suspend fun login(emailOrUsername: String, password: String): LoginResponse{
        val response: LoginResponse = apiClient.post("http://192.168.0.235:4000/auth/login") {
            contentType(ContentType.Application.Json)
            body = LoginRequestBody(emailOrUsername, password)
        }

        println("login response: $response")

        return response
    }


}