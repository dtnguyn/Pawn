package com.nguyen.pawn.repo

import android.annotation.SuppressLint
import android.util.Log
import com.nguyen.pawn.api.model.ApiResponse
import com.nguyen.pawn.api.model.GetDailyWordsRequestBody
import com.nguyen.pawn.api.model.PickLearningLanguagesRequestBody
import com.nguyen.pawn.api.model.RegisterRequestBody
import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.pawn.db.mapper.DailyWordMapper
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.util.Constants.apiURL
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class WordRepository
@Inject constructor(
    private val apiClient: HttpClient,
    private val db: PawnDatabase
) {
    companion object {
        private const val TAG = "WordRepo"
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun getRandomDailyWord(wordCount: Int, language: String): ArrayList<Word> {
        return try {
            val cacheDailyWords = db.dailyWordDao().getMany(SimpleDateFormat("yyyy.MM.dd").format(Date()), language)
            if(cacheDailyWords.isNotEmpty()) {
                DailyWordMapper.mapToListNetworkEntity(cacheDailyWords) as ArrayList<Word>
            } else {
                val response: ApiResponse<ArrayList<Word>?> = apiClient.get("${apiURL}/word/daily?language=${language}&dailyWordCount=${wordCount}")
                if(response.status){
                    db.dailyWordDao().clearAll(language = language)
                    db.dailyWordDao().insertMany(DailyWordMapper.mapToListCacheEntity(response.data!!))
                    response.data
                } else {
                    arrayListOf()
                }
            }
        } catch (error: ClientRequestException) {
            Log.d(TAG, "getRandomDailyWord error: ${error.message}")
            arrayListOf()
        } catch (error: ConnectException) {
            Log.d(TAG, "getRandomDailyWord error: ${error.message}")
            arrayListOf()
        }
    }
}