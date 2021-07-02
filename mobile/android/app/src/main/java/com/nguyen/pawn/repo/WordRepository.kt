package com.nguyen.pawn.repo

import android.util.Log
import com.nguyen.pawn.api.model.PickLearningLanguagesRequestBody
import com.nguyen.pawn.api.model.RegisterRequestBody
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class WordRepository
@Inject constructor(
    private val apiClient: HttpClient
){

    suspend fun pickLearningLanguages(languages: List<String>, accessToken: String): Boolean {
         return try {
            val response: Boolean = apiClient.post("http://192.168.0.235:4000/word/save/language") {
                contentType(ContentType.Application.Json)
                body = PickLearningLanguagesRequestBody(languages)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
             response
        } catch (error: ClientRequestException) {
            Log.d("Auth", "error: ${error.message}")
            false
        }
    }

}