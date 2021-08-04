package com.nguyen.pawn.repo

import android.util.Log
import com.nguyen.pawn.api.model.ApiResponse
import com.nguyen.pawn.api.model.PickLearningLanguagesRequestBody
import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.pawn.db.entity.LanguageCacheEntity
import com.nguyen.pawn.db.mapper.LanguageMapper
//import com.nguyen.pawn.db.PawnDatabase
//import com.nguyen.pawn.db.entity.LanguageCacheEntity
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.repo.utils.mainGetNetworkBoundResource
import com.nguyen.pawn.repo.utils.mainPostNetworkBoundResource
import com.nguyen.pawn.util.Constants
import com.nguyen.pawn.util.CustomAppException
import com.nguyen.pawn.util.UIState
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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


    suspend fun pickLearningLanguages(languages: List<Language>, accessToken: String?): Flow<UIState<List<Language>>> {

        return mainPostNetworkBoundResource(
            submit = {
                val languagesString = languages.map { language ->
                    language.id
                }
                apiClient.post<ApiResponse<Void>>("${Constants.apiURL}/language/save") {
                    contentType(ContentType.Application.Json)
                    body = PickLearningLanguagesRequestBody(languagesString )
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
                languages
            },
            shouldSave = {
                true
            },
            saveSubmitResult = {
                Log.d(TAG, "saving to cache")
                database.languageDao().clearAll()
                database.languageDao().insertMany(LanguageMapper.mapToCacheEntityList(languages))
            }
        )
    }

     fun getLearningLanguages(accessToken: String?): Flow<UIState<List<Language>>>{

        return mainGetNetworkBoundResource(
            query = {
                database.languageDao().getMany().map {
                    LanguageMapper.mapFromCacheEntityList(it)
                }
            },
            fetch = {
                val response: ApiResponse<ArrayList<Language>> = apiClient.get("${Constants.apiURL}/language/") {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
                response.data
            },
            saveFetchResult = {languages ->
                languages?.let {
                    database.languageDao().clearAll()
                    database.languageDao().insertMany(LanguageMapper.mapToCacheEntityList(it))
                }
            }
        )

    }
}