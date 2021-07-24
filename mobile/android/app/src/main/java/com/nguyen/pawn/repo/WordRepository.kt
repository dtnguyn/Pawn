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
import java.net.ConnectException
import javax.inject.Inject

class WordRepository
@Inject constructor(
    private val apiClient: HttpClient
) {
    companion object {
        private const val TAG = "WordRepo"
    }

    suspend fun getRandomDailyWord(wordCount: Int, language: String): ArrayList<Word> {
        return try {
            val response: ApiResponse<ArrayList<Word>?> = apiClient.get("${apiURL}/word/daily?language=${language}&dailyWordCount=${wordCount}")
            Log.d(TAG, "Response $language: $response")
            response.data ?: arrayListOf()
        } catch (error: ClientRequestException) {
            Log.d(TAG, "getRandomDailyWord error: ${error.message}")
            arrayListOf()
        } catch (error: ConnectException) {
            Log.d(TAG, "getRandomDailyWord error: ${error.message}")
            arrayListOf()
        }
    }
}