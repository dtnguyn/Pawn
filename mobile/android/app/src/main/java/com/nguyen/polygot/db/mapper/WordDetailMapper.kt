package com.nguyen.polygot.db.mapper

import android.util.Log
import com.nguyen.polygot.db.entity.WordDetailCacheEntity
import com.nguyen.polygot.model.WordDetail
import java.util.*

object WordDetailMapper {

    fun mapToCacheEntity(word: WordDetail): WordDetailCacheEntity {
        Log.d("SavedWordMapper", "debug: $word")
        return WordDetailCacheEntity(
            id = UUID.randomUUID().toString(),
            value = word.value,
            language = word.language,
            definitions = word.definitions,
            pronunciations = word.pronunciations
        )
    }

    fun mapToNetworkEntity(word: WordDetailCacheEntity): WordDetail {
        return WordDetail(
            value = word.value,
            language = word.language,
            definitions = word.definitions,
            pronunciations = word.pronunciations,
        )
    }

    fun mapToListCacheEntity(words: List<WordDetail>): List<WordDetailCacheEntity> {
        return words.map{
            mapToCacheEntity(it)
        }
    }

    fun mapToListNetworkEntity(words: List<WordDetailCacheEntity>): List<WordDetail> {
        return words.map{
            mapToNetworkEntity(it)
        }
    }
}