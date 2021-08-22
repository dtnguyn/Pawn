package com.nguyen.pawn.repo.utils

import android.util.Log
import com.nguyen.pawn.util.CustomAppException
import com.nguyen.pawn.util.UIState
import io.ktor.client.features.*
import kotlinx.coroutines.flow.*
import java.net.ConnectException

inline fun <DomainType> mainGetNetworkBoundResource(
    crossinline query: () -> Flow<DomainType?>,
    crossinline fetch: suspend () -> DomainType?,
    crossinline saveFetchResult: suspend (DomainType?) -> Unit,
    crossinline shouldFetch: (DomainType?) -> Boolean = { true },
    tag: String = "NetworkBoundResource",
): Flow<UIState<DomainType>> {
    return flow {
        try {
            emit(UIState.Loading())

            val cacheData = query().first()

            if(cacheData != null){
                emit(UIState.Loaded<DomainType>(cacheData))
            }

            if(shouldFetch(cacheData)){
                saveFetchResult(fetch())

                emitAll(query().map {
//                    Log.d(tag, "query word detail: ${it}")
                    UIState.Loaded<DomainType>(it) }
                )
            }
        } catch(error: CustomAppException){
            Log.d(tag, "CustomAppException: ${error.message}")
            emit(UIState.Error<DomainType>(error.message))
        } catch (error: ClientRequestException) {
            Log.d(tag, "ClientRequestException: ${error.message}")
            emit(UIState.Error<DomainType>("Something went wrong!"))
        } catch (error: ConnectException) {
            Log.d(tag, "ConnectException: ${error.message}")
            emit(UIState.Error<DomainType>("Something went wrong with connection!"))
        }

    }
}


inline fun <ResponseType> mainPostNetworkBoundResource(
    crossinline submit: suspend () -> ResponseType?,
    crossinline shouldSave: suspend (response: ResponseType?) -> Boolean,
    crossinline saveSubmitResult: suspend (ResponseType?) -> Unit,
): Flow<UIState<ResponseType>> {
    return flow {
        try {
            emit(UIState.Loading())
            val response = submit()
            if(shouldSave(response)){
                saveSubmitResult(response)
            }
            emit(UIState.Loaded(response))


        } catch(error: CustomAppException){
            emit(UIState.Error<ResponseType>(error.message))
        } catch (error: ClientRequestException) {
            emit(UIState.Error<ResponseType>("Something went wrong!"))
        } catch (error: ConnectException) {
            emit(UIState.Error<ResponseType>("Something went wrong with connection!"))
        }

    }
}
