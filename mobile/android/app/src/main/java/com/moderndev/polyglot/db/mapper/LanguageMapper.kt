package com.moderndev.polyglot.db.mapper

import com.moderndev.polyglot.db.entity.LanguageCacheEntity
import com.moderndev.polyglot.model.Language

object LanguageMapper {
    fun mapToCacheEntity(language: Language): LanguageCacheEntity{
        return LanguageCacheEntity(id = language.id, value = language.value)
    }

    fun mapToCacheEntityList(languages: List<Language>): List<LanguageCacheEntity>{
        return languages.map{ mapToCacheEntity(it) }
    }

    fun mapFromCacheEntity(languageCache: LanguageCacheEntity): Language{
        return Language(id = languageCache.id, value = languageCache.value)
    }

    fun mapFromCacheEntityList(languagesCache: List<LanguageCacheEntity>): List<Language>{
        return languagesCache.map { mapFromCacheEntity(it) }
    }


}