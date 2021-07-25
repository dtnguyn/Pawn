package com.nguyen.pawn.db.mapper

import android.annotation.SuppressLint
import com.nguyen.pawn.db.entity.DailyWordCacheEntity
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.model.Pronunciation
import com.nguyen.pawn.model.Word
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DailyWordMapper {

    @SuppressLint("SimpleDateFormat")
    fun mapToCacheEntity(word: Word): DailyWordCacheEntity {
        return DailyWordCacheEntity(
            id = UUID.randomUUID().toString(),
            value = word.value,
            language = word.language,
            display = true,
            pronunciationAudio = word.pronunciations[0].audio,
            pronunciationSymbol = word.pronunciations[0].symbol,
            mainDefinition = word.definitions[0].meaning,
            createdDate = SimpleDateFormat("yyyy.MM.dd").format(Date())
        )
    }

    fun mapToNetworkEntity(word: DailyWordCacheEntity): Word {
        return Word(
            value = word.value,
            language = word.language,
            pronunciations = listOf(Pronunciation(audio = word.pronunciationAudio, symbol = word.pronunciationSymbol)),
            definitions = listOf(Definition(meaning = word.mainDefinition, example = "", partOfSpeech = "")),
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