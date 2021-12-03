package com.nguyen.polyglot.db.mapper

import android.annotation.SuppressLint
import android.util.Log
import com.nguyen.polyglot.db.entity.SavedWordCacheEntity
import com.nguyen.polyglot.model.Word
import java.text.SimpleDateFormat
import java.util.*

object SavedWordMapper {

    @SuppressLint("SimpleDateFormat")
    fun mapToCacheEntity(word: Word): SavedWordCacheEntity {
        Log.d("SavedWordMapper", "debug: $word")
        return SavedWordCacheEntity(
            id = UUID.randomUUID().toString(),
            value = word.value,
            language = word.language,
            pronunciationAudio = word.pronunciationAudio,
            pronunciationSymbol = word.pronunciationSymbol,
            topics = word.topics,
            mainDefinition = word.mainDefinition,
            createdDate = SimpleDateFormat("yyyy.MM.dd").format(Date())
        )
    }

    fun mapToNetworkEntity(word: SavedWordCacheEntity): Word {
        return Word(
            value = word.value,
            language = word.language,
            mainDefinition = word.mainDefinition,
            topics = word.topics,
            pronunciationAudio = word.pronunciationAudio,
            pronunciationSymbol = word.pronunciationSymbol
        )
    }

    fun mapToListCacheEntity(words: List<Word>): List<SavedWordCacheEntity> {
        return words.map{
            mapToCacheEntity(it)
        }
    }

    fun mapToListNetworkEntity(words: List<SavedWordCacheEntity>): List<Word> {
        return words.map{
            mapToNetworkEntity(it)
        }
    }

}