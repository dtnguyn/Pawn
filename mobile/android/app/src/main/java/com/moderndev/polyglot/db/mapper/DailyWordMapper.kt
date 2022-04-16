package com.moderndev.polyglot.db.mapper

import android.annotation.SuppressLint
import com.moderndev.polyglot.db.entity.DailyWordCacheEntity
import com.moderndev.polyglot.model.Word
import java.text.SimpleDateFormat
import java.util.*

object DailyWordMapper {

    @SuppressLint("SimpleDateFormat")
    fun mapToCacheEntity(word: Word, display: Boolean = true): DailyWordCacheEntity {
        return DailyWordCacheEntity(
            id = UUID.randomUUID().toString(),
            value = word.value,
            language = word.language,
            display = display,
            pronunciationAudio = word.pronunciationAudio,
            pronunciationSymbol = word.pronunciationSymbol,
            topics = word.topics,
            mainDefinition = word.mainDefinition,
            createdDate = SimpleDateFormat("yyyy.MM.dd").format(Date())
        )
    }

    fun mapToNetworkEntity(word: DailyWordCacheEntity): Word {
        return Word(
            value = word.value,
            language = word.language,
            mainDefinition = word.mainDefinition,
            pronunciationSymbol = word.pronunciationSymbol,
            pronunciationAudio = word.pronunciationAudio,
            topics = word.topics
        )
    }

    fun mapToListCacheEntity(words: List<Word>): List<DailyWordCacheEntity> {
        return words.map{
            mapToCacheEntity(it)
        }
    }

    fun mapToListNetworkEntity(words: List<DailyWordCacheEntity>): List<Word> {
        return words.map{
            mapToNetworkEntity(it)
        }
    }

}