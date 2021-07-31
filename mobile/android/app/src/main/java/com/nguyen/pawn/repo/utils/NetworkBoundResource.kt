package com.nguyen.pawn.repo.utils

import android.util.Log
import com.nguyen.pawn.repo.AuthRepository
import com.nguyen.pawn.util.CustomAppException
import com.nguyen.pawn.util.UIState
import io.ktor.client.features.*
import kotlinx.coroutines.flow.*
import java.net.ConnectException

inline fun <DomainType> mainNetworkBoundResource(
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
                emit(UIState.Idle<DomainType>(cacheData))
            }

            if(shouldFetch(cacheData)){
                saveFetchResult(fetch())
                emitAll(query().map { UIState.Idle<DomainType>(it) })
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
