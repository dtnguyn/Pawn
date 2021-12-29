package com.nguyen.polyglot.util

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.theme.*
import com.nguyen.polyglot.util.Constants.allLanguages
import java.text.ParseException
import java.text.SimpleDateFormat

object UtilFunctions {

    private val pastelMap = HashMap<String, Color>()
    private var currentPastelIndex = 0

    fun generateRandomPastelColor(word: String = ""): Color {
        if(word.isEmpty()) return PastelColors[(PastelColors.indices).random()]
        return if(pastelMap[word] != null){
            pastelMap[word]!!
        } else {
            val color = PastelColors[currentPastelIndex]
            currentPastelIndex++
            if(currentPastelIndex >= PastelColors.size) currentPastelIndex = 0
            pastelMap[word] = color
            color
        }
    }

    fun fromLanguageId(id: String): String? {
        Log.d("debug", "id $id")
        val languageId = id.subSequence(0,2)
        val languages = allLanguages.filter { it.id == languageId }
        return if(languages.isEmpty()) null
        else languages[0].value
    }

    fun convertHeightToDp(pixel: Int, displayMetrics: DisplayMetrics): Int {
        return (pixel / (displayMetrics.density)).toInt()
    }

    fun generateBackgroundColorForLanguage(language: String): Color {
        return when (language) {
            SupportedLanguage.ENGLISH.id -> SupportedLanguage.ENGLISH.backgroundColor
            SupportedLanguage.GERMANY.id -> SupportedLanguage.GERMANY.backgroundColor
            SupportedLanguage.FRENCH.id -> SupportedLanguage.FRENCH.backgroundColor
            SupportedLanguage.SPANISH.id -> SupportedLanguage.SPANISH.backgroundColor
            else -> Color.Transparent
        }
    }

    fun generateFlagForLanguage(language: String): Int {
        return when (language) {
            SupportedLanguage.ENGLISH.id -> SupportedLanguage.ENGLISH.flag
            SupportedLanguage.GERMANY.id -> SupportedLanguage.GERMANY.flag
            SupportedLanguage.FRENCH.id -> SupportedLanguage.FRENCH.flag
            SupportedLanguage.SPANISH.id -> SupportedLanguage.SPANISH.flag
            else -> R.drawable.world
        }
    }

    fun generateIconForLanguage(language: String): Int {
        return when (language) {
            SupportedLanguage.ENGLISH.id -> SupportedLanguage.ENGLISH.icon
            SupportedLanguage.GERMANY.id -> SupportedLanguage.GERMANY.icon
            SupportedLanguage.FRENCH.id -> SupportedLanguage.FRENCH.icon
            SupportedLanguage.SPANISH.id -> SupportedLanguage.SPANISH.icon
            else -> SupportedLanguage.ENGLISH.icon
        }
    }

    fun reformatString(str: String) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(str).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun reformatDateString(dateString: String?): String?{
        try {
            if(dateString == null) return null
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy, Ka").format(date)
            return formattedDate
        } catch (error: ParseException){
            return null
        }

    }
}