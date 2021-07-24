package com.nguyen.pawn.repo

import android.util.Log
import com.nguyen.pawn.api.model.ApiResponse
import com.nguyen.pawn.api.model.PickLearningLanguagesRequestBody
import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.pawn.db.entity.LanguageCacheEntity
//import com.nguyen.pawn.db.PawnDatabase
//import com.nguyen.pawn.db.entity.LanguageCacheEntity
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.util.Constants
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.ConnectException
import javax.inject.Inject

class LanguageRepository
@Inject constructor(
    private val apiClient: HttpClient,
    private val database: PawnDatabase
){
    companion object{
        private const val TAG = "LanguageRepo"
    }


    suspend fun pickLearningLanguages(languages: List<Language>, accessToken: String?): Boolean {
        return try {
            val languagesString = languages.map { language ->
                language.id
            }
            val response: ApiResponse<Void> = apiClient.post("${Constants.apiURL}/language/save") {
                contentType(ContentType.Application.Json)
                body = PickLearningLanguagesRequestBody(languagesString )
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
            val cacheLanguages = languages.map {language ->
                LanguageCacheEntity(id = language.id, value = language.value)
            }

            Log.d(TAG, "save cache ${response.status} $accessToken")

            if(response.status || accessToken == null){
                database.languageDao().clearAll()
                database.languageDao().insertMany(cacheLanguages)
                true
            } else {
                response.status
            }

        } catch (error: ClientRequestException) {
            Log.d(TAG, "pickLearningLanguages error: ${error.message}")
            false
        } catch (error: ConnectException) {
            Log.d(TAG, "pickLearningLanguages error: ${error.message}")
            false
        }
    }

    suspend fun getLearningLanguages(accessToken: String?): List<Language>{
        return try {
            val response: ApiResponse<ArrayList<Language>> = apiClient.get("${Constants.apiURL}/language/") {
                contentType(ContentType.Application.Json)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }
            if(response.status) {
                val languages = response.data
                val cacheLanguages = languages.map {language ->
                    LanguageCacheEntity(id = language.id, value = language.value)
                }
                database.languageDao().clearAll()
                database.languageDao().insertMany(cacheLanguages)

                languages
            } else {
                val cacheLanguages = database.languageDao().getMany()

                val languages = cacheLanguages.map { language ->
                    Language(id = language.id, value = language.value)
                }
                Log.d(TAG, "cacheLanguages $languages")
                languages
            }

        } catch (error: ClientRequestException) {
            Log.d(TAG, "getLearningLanguages error: ${error.message}")
            arrayListOf()
        } catch (error: ConnectException) {
            Log.d(TAG, "getLearningLanguages error: ${error.message}")
            arrayListOf()
        }
    }
}