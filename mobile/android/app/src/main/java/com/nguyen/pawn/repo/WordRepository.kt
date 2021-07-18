package com.nguyen.pawn.repo

import android.util.Log
import com.nguyen.pawn.api.model.ApiResponse
import com.nguyen.pawn.api.model.GetDailyWordsRequestBody
import com.nguyen.pawn.api.model.PickLearningLanguagesRequestBody
import com.nguyen.pawn.api.model.RegisterRequestBody
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.util.Constants.apiURL
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class WordRepository
@Inject constructor(
    private val apiClient: HttpClient
){

    suspend fun getRandomDailyWord(wordCount: Int, language: String): List<Word>? {
         return try {
            val response: ApiResponse<List<Word>?> = apiClient.post("${apiURL}/word/daily") {
                contentType(ContentType.Application.Json)
                body = GetDailyWordsRequestBody(wordCount, language)
            }
             response.data
        } catch (error: ClientRequestException) {
            null
        }
    }





}