package com.nguyen.pawn.repo

import android.annotation.SuppressLint
import com.nguyen.pawn.api.model.ApiResponse
import com.nguyen.pawn.db.PawnDatabase
import com.nguyen.pawn.db.mapper.DailyWordMapper
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.repo.utils.mainGetNetworkBoundResource
import com.nguyen.pawn.util.Constants.apiURL
import com.nguyen.pawn.util.CustomAppException
import com.nguyen.pawn.util.UIState
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    suspend fun getRandomDailyWord(wordCount: Int, language: String): Flow<UIState<List<Word>>> {
        return mainGetNetworkBoundResource(
            query = {
                db.dailyWordDao().getMany(SimpleDateFormat("yyyy.MM.dd").format(Date()), language)
                    .map { if (it.isEmpty()) null else  DailyWordMapper.mapToListNetworkEntity(it) }
            },
            fetch = {
                val response: ApiResponse<ArrayList<Word>?> =
                    apiClient.get("${apiURL}/word/daily?language=${language}&dailyWordCount=${wordCount}")
                if (response.status) response.data
                else throw CustomAppException(response.message)
            },
            saveFetchResult = {words ->
                words?.let {
                    db.dailyWordDao().clearAll(language = language)
                    db.dailyWordDao().insertMany(DailyWordMapper.mapToListCacheEntity(it))
                }
            },
            shouldFetch ={
                it.isNullOrEmpty()
            },
            tag = TAG
        )
    }
}