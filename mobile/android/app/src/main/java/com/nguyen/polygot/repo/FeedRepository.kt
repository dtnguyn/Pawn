package com.nguyen.polygot.repo

import com.nguyen.polygot.api.model.ApiResponse
import com.nguyen.polygot.db.PolygotDatabase
import com.nguyen.polygot.db.mapper.FeedMapper
import com.nguyen.polygot.model.Feed
import com.nguyen.polygot.repo.utils.mainGetNetworkBoundResource
import com.nguyen.polygot.util.Constants
import com.nguyen.polygot.util.CustomAppException
import com.nguyen.polygot.util.UIState
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FeedRepository
@Inject constructor(
    private val apiClient: HttpClient,
    private val database: PolygotDatabase
){

    fun getFeeds(accessToken: String, language: String): Flow<UIState<List<Feed>>>{
        return mainGetNetworkBoundResource(
            query = {
                database.feedDao().getMany(language).map {
                    FeedMapper.mapToListNetworkEntity(it)
                }
            },
            shouldFetch = {
                  true
            },
            fetch = {
                val response: ApiResponse<List<Feed> = apiClient.get("${Constants.apiURL}/feed/"){
                    contentType(ContentType.Application.Json)
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
                if(response.status) response.data
                else throw CustomAppException(response.message)

            },
            saveFetchResult = {feeds ->
                feeds?.let {
                    database.feedDao().insertMany()
                }
            }
        )
    }


}