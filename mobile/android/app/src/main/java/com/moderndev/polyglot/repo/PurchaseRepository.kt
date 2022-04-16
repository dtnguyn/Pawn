package com.moderndev.polyglot.repo

import com.moderndev.polyglot.api.model.ApiResponse
import com.moderndev.polyglot.api.model.PurchaseNetworkModels
import com.moderndev.polyglot.db.PolyglotDatabase
import com.moderndev.polyglot.util.Constants
import com.moderndev.polyglot.util.UIState
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
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