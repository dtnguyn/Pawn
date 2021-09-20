package com.nguyen.polygot.repo

import android.annotation.SuppressLint
import android.util.Log
import com.nguyen.polygot.api.model.ApiResponse
import com.nguyen.polygot.db.PolygotDatabase
import com.nguyen.polygot.db.mapper.FeedMapper
//import com.nguyen.polygot.db.mapper.FeedMapper
import com.nguyen.polygot.model.Feed
import com.nguyen.polygot.repo.utils.mainGetNetworkBoundResource
import com.nguyen.polygot.util.Constants
import com.nguyen.polygot.util.CustomAppException
import com.nguyen.polygot.util.UIState
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


private const val TAG = "FeedRepository"

class FeedRepository
@Inject constructor(
    private val apiClient: HttpClient,
    private val database: PolygotDatabase
) {

    @SuppressLint("SimpleDateFormat")
    fun getFeeds(accessToken: String, language: String): Flow<UIState<List<Feed>>> {
        return mainGetNetworkBoundResource(
            query = {
                database.feedDao().getMany("en").map {
                    FeedMapper.mapToListNetworkEntity(it)
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
                val response: ApiResponse<List<Feed>> = apiClient.get("${Constants.apiURL}/feed?language=${language}") {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
                if (response.status) {
                    Log.d(TAG, "response: ${response.data.size}")
                    response.data
                }
                else throw CustomAppException(response.message)

            },
            saveFetchResult = { feeds ->
                feeds?.let {
                    database.feedDao().insertMany(FeedMapper.mapToListCacheEntity(it))
                }
            }
        )
    }


}