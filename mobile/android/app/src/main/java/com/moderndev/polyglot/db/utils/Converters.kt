package com.moderndev.polyglot.db.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.moderndev.polyglot.model.Definition
import com.moderndev.polyglot.model.Pronunciation

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromDefinitionList(definitions: List<Definition>) = gson.toJson(definitions)

    @TypeConverter
    fun toDefinitionList(definitions: String) = gson.fromJson(definitions, Array<Definition>::class.java).toList()

    @TypeConverter
    fun fromPronunciationList(pronunciations: List<Pronunciation>) = gson.toJson(pronunciations)

    @TypeConverter
    fun toPronunciationList(pronunciations: String) = gson.fromJson(pronunciations, Array<Pronunciation>::class.java).toList()

}