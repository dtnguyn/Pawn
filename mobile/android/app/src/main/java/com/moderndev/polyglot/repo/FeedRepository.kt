package com.moderndev.polyglot.repo

import android.annotation.SuppressLint
import android.util.Log
import com.moderndev.polyglot.api.model.ApiResponse
import com.moderndev.polyglot.api.model.UpdateFeedTopicsRequestBody
import com.moderndev.polyglot.db.PolyglotDatabase
import com.moderndev.polyglot.db.mapper.FeedMapper
import com.moderndev.polyglot.model.*
//import com.nguyen.polygot.db.mapper.FeedMapper
import com.moderndev.polyglot.repo.utils.mainGetNetworkBoundResource
import com.moderndev.polyglot.repo.utils.mainPostNetworkBoundResource
import com.moderndev.polyglot.util.Constants
import com.moderndev.polyglot.util.CustomAppException
import com.moderndev.polyglot.util.UIState
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


private const val TAG = "FeedRepository"

class FeedRepository
@Inject constructor(
    private val apiClient: HttpClient,
    private val database: PolyglotDatabase
) {

    @SuppressLint("SimpleDateFormat")
    fun getFeeds(accessToken: String, language: String, topics: String): Flow<UIState<List<Feed>>> {
        return mainGetNetworkBoundResource(
            query = {
                if(topics.isEmpty()){
                    database.feedDao().getMany(language).map {
                        if (it.isEmpty()) {
                            null
                        } else {
                            FeedMapper.mapToListNetworkEntity(it)
                        }
                    }
                } else {
                    database.feedDao().getManyWithFilter(language, topics).map {
                        if (it.isEmpty()) {
                            null
                        } else {
                            FeedMapper.mapToListNetworkEntity(it)
                        }
                    }
//                    var filterString = ""
//                    topics.forEachIndexed { index, topic ->
//                        filterString += if(index != topic.length - 1) {
//                            "topic = $topic OR"
//                        } else {
//                            "topic = $topic"
//                        }
//                    }
//                    val queryString = "SELECT * FROM feeds WHERE language = $language AND ( $filterString )"
//                    val query = SimpleSQLiteQuery(queryString)
//                    database.feedDao().getManyWithFilter(query).map {
//                        if (it.isEmpty()) {
//                            null
//                        } else {
//                            FeedMapper.mapToListNetworkEntity(it)
//                        }
//                    }
                }

            },
            shouldFetch = {
                runBlocking {
                    val feeds = database.feedDao().getManyWithDate(
                        language, SimpleDateFormat("yyyy.MM.dd").format(
                            Date()
                        )
                    ).first()
                    feeds.isEmpty()
                }
            },
            fetch = {
                val response: ApiResponse<List<Feed>> =
                    apiClient.get("${Constants.apiURL}/feed?language=${language}") {
                        contentType(ContentType.Application.Json)
                        headers {
                            append(HttpHeaders.Authorization, "Bearer $accessToken")
                        }
                    }
                Log.d(TAG, "feed response: ${response}")
                if (response.status) {
                    response.data
                } else throw CustomAppException(response.message)

            },
            saveFetchResult = { feeds ->
                feeds?.let {
                    database.feedDao().insertMany(FeedMapper.mapToListCacheEntity(it))
                }
            }
        )
    }


    fun getTopics(accessToken: String): Flow<UIState<String>> {

        var topics: String? = null
        return mainGetNetworkBoundResource(
            query = {
                flow { emit(topics) }
            },
            shouldFetch = {
                true
            },
            fetch = {
                val response: ApiResponse<String> =
                    apiClient.get("${Constants.apiURL}/feed/topics") {
                        contentType(ContentType.Application.Json)
                        headers {
                            append(HttpHeaders.Authorization, "Bearer $accessToken")
                        }
                    }
                if (response.status) {
                    response.data
                } else throw CustomAppException(response.message)

            },
            saveFetchResult = {
                topics = it ?: ""
            }
        )
    }

    fun updateTopics(accessToken: String, newTopics: String): Flow<UIState<String>> {
        return mainPostNetworkBoundResource(
            submit = {
                val response: ApiResponse<String> =
                    apiClient.post("${Constants.apiURL}/feed/topics") {
                        contentType(ContentType.Application.Json)
                        headers {
                            append(HttpHeaders.Authorization, "Bearer $accessToken")
                        }
                        body = UpdateFeedTopicsRequestBody(newTopics)
                    }
                if (response.status) {
                    Log.d("FeedRepository", "topic response: $response")
                    response.data
                } else throw CustomAppException(response.message)
            },
            shouldSave = { false },
            saveSubmitResult = {}
        )
    }


    fun getNewsDetail(
        accessToken: String,
        id: String,
        url: String
    ): Flow<UIState<FeedDetail<NewsDetail>>> {
        var detail: FeedDetail<NewsDetail>? = null
        return mainGetNetworkBoundResource(
            query = {
                flow {
                    emit(detail)
                }
            },
            fetch = {
                val response: ApiResponse<FeedDetail<NewsDetail>> =
                    apiClient.get("${Constants.apiURL}/feed/detail?url=${url}&id=${id}&type=news") {
                        contentType(ContentType.Application.Json)
                        headers {
                            append(HttpHeaders.Authorization, "Bearer $accessToken")
                        }
                    }
                if (response.status) {
                    Log.d(TAG, "getNewsDetail: ${response.data}")
                    response.data
                } else throw CustomAppException(response.message)
            },
            saveFetchResult = {
                detail = it
            }
        )
    }

    fun getWordDefinition(
        accessToken: String,
        word: String,
        language: String
    ): Flow<UIState<Word>> {
        var wordDefinition: Word? = null
        return mainGetNetworkBoundResource(
            query = {
                flow {
                    emit(wordDefinition)
                }
            },
            fetch = {
                val response: ApiResponse<Word> =
                    apiClient.get("${Constants.apiURL}/feed/word/definition?word=${word}&language=${language}") {
                        contentType(ContentType.Application.Json)
                        headers {
                            append(HttpHeaders.Authorization, "Bearer $accessToken")
                        }
                    }
                if (response.status) {
                    Log.d(TAG, "getWordDefinition: ${response}")
                    response.data
                } else throw CustomAppException(response.message)
            },
            saveFetchResult = {
                wordDefinition = it
            }
        )
    }

    fun getVideoSubtitle(
        videoId: String,
        language: String,
        translatedLanguage: String,
        accessToken: String
    ): Flow<UIState<List<SubtitlePart>>> {
        Log.d("FeedRepo", "translatedLanguage $translatedLanguage")
        var subtitle: List<SubtitlePart>? = null
        return mainGetNetworkBoundResource(
            query = {
                flow {
                    emit(subtitle)
                }
            },
            fetch = {
                Log.d("VideoDetailViewModel", "Call to get subtitle")
                val response: ApiResponse<List<SubtitlePart>> =
                    apiClient.get("${Constants.apiURL}/feed/video/subtitle?videoId=${videoId}&language=${language}&translatedLanguage=${translatedLanguage}") {
                        contentType(ContentType.Application.Json)
                        headers {
                            append(HttpHeaders.Authorization, "Bearer $accessToken")
                        }
                    }
                Log.d(TAG, "getVideoSubtitle: ${response}")

                if (response.status) {
                    response.data
                } else throw CustomAppException(response.message)
            },
            saveFetchResult = {
                subtitle = it
            }
        )
    }

}