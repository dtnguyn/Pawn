package com.nguyen.pawn.repo

import android.util.Log
import com.nguyen.pawn.api.model.ApiResponse
import com.nguyen.pawn.api.model.PickLearningLanguagesRequestBody
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.util.Constants
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class LanguageRepository
@Inject constructor(
    private val apiClient: HttpClient
){
    companion object{
        private const val TAG = "LanguageRepo"
    }


    suspend fun pickLearningLanguages(languages: List<String>, accessToken: String): Boolean {
        return try {
            val response: ApiResponse<Void> = apiClient.post("${Constants.apiURL}/language/save") {
                contentType(ContentType.Application.Json)
                body = PickLearningLanguagesRequestBody(languages)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
            response.status
        } catch (error: ClientRequestException) {
            Log.d(TAG, "error: ${error.message}")
            false
        }
    }

    suspend fun getLearningLanguages(accessToken: String): ArrayList<Language>{
        return try {
            val response: ApiResponse<ArrayList<Language>> = apiClient.get("${Constants.apiURL}/language/") {
                contentType(ContentType.Application.Json)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
            response.data
        } catch (error: ClientRequestException) {
            Log.d(TAG, "error: ${error.message}")
            arrayListOf()
        }
    }
}