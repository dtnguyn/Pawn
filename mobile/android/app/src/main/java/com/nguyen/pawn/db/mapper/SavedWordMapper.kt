package com.nguyen.pawn.db.mapper

import android.annotation.SuppressLint
import android.util.Log
import com.nguyen.pawn.db.entity.DailyWordCacheEntity
import com.nguyen.pawn.db.entity.SavedWordCacheEntity
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.model.Pronunciation
import com.nguyen.pawn.model.Word
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object SavedWordMapper {

    @SuppressLint("SimpleDateFormat")
    fun mapToCacheEntity(word: Word): SavedWordCacheEntity {
        Log.d("SavedWordMapper", "debug: $word")
        return SavedWordCacheEntity(
            id = UUID.randomUUID().toString(),
            value = word.value,
            language = word.language,
            pronunciationAudio = if (word.pronunciations.isNotEmpty()) word.pronunciations[0].audio else null,
            pronunciationSymbol = if (word.pronunciations.isNotEmpty()) word.pronunciations[0].symbol else null,
            mainDefinition = if (word.definitions.isNotEmpty()) word.definitions[0].meaning else "",
            createdDate = SimpleDateFormat("yyyy.MM.dd").format(Date())
        )
    }

    fun mapToNetworkEntity(word: SavedWordCacheEntity): Word {
        return Word(
            value = word.value,
            language = word.language,
            pronunciations = listOf(Pronunciation(audio = word.pronunciationAudio, symbol = word.pronunciationSymbol)),
            definitions = listOf(Definition(meaning = word.mainDefinition, example = "", partOfSpeech = "")),
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