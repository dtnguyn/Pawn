package com.nguyen.polyglot.repo

import com.nguyen.polyglot.api.model.ApiResponse
import com.nguyen.polyglot.api.model.PickLearningLanguagesRequestBody
import com.nguyen.polyglot.api.model.PurchaseNetworkModels
import com.nguyen.polyglot.db.PolyglotDatabase
import com.nguyen.polyglot.model.Language
import com.nguyen.polyglot.util.Constants
import com.nguyen.polyglot.util.UIState
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PurchaseRepository
@Inject constructor(
    private val apiClient: HttpClient,
    private val database: PolyglotDatabase
){

    suspend fun purchasePremium(accessToken: String, orderId: String, token: String, time: String): UIState<Boolean> {

        val response: ApiResponse<Boolean> = apiClient.post("${Constants.apiURL}/purchase/") {
            contentType(ContentType.Application.Json)
            body = PurchaseNetworkModels(orderId, token, time)
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }
        return if(response.status){
            UIState.Loaded(response.data)
        } else {
            UIState.Error(response.message)
        }
    }




}