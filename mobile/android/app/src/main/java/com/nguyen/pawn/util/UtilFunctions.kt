package com.nguyen.pawn.util

import android.util.DisplayMetrics
import androidx.compose.ui.graphics.Color
import com.nguyen.pawn.ui.theme.LightGreen
import com.nguyen.pawn.ui.theme.LightOrange
import com.nguyen.pawn.ui.theme.LightRed
import com.nguyen.pawn.ui.theme.Neon

object UtilFunctions {

    fun generateColor(index: Int): Color {
        return when {
            index % 4 == 0 -> LightGreen
            index % 3 == 0 -> LightOrange
            index % 2 == 0 -> Neon
            else -> LightRed
        }
    }

    fun convertHeightToDp(pixel: Int, displayMetrics: DisplayMetrics): Int {
        return (pixel / (displayMetrics.density)).toInt()
    }

    fun generateBackgroundColorForLanguage(language: String): Color {
        return when (language) {
            SupportedLanguage.ENGLISH.value -> SupportedLanguage.ENGLISH.backgroundColor
            SupportedLanguage.GERMANY.value -> SupportedLanguage.GERMANY.backgroundColor
            SupportedLanguage.FRENCH.value -> SupportedLanguage.FRENCH.backgroundColor
            SupportedLanguage.SPANISH.value -> SupportedLanguage.SPANISH.backgroundColor
            else -> SupportedLanguage.ENGLISH.backgroundColor
        }
    }

    fun generateFlagForLanguage(language: String): Int {
        return when (language) {
            SupportedLanguage.ENGLISH.value -> SupportedLanguage.ENGLISH.flag
            SupportedLanguage.GERMANY.value -> SupportedLanguage.GERMANY.flag
            SupportedLanguage.FRENCH.value -> SupportedLanguage.FRENCH.flag
            SupportedLanguage.SPANISH.value -> SupportedLanguage.SPANISH.flag
            else -> SupportedLanguage.ENGLISH.flag
        }
    }

    fun generateIconForLanguage(language: String): Int {
        return when (language) {
            SupportedLanguage.ENGLISH.value -> SupportedLanguage.ENGLISH.icon
            SupportedLanguage.GERMANY.value -> SupportedLanguage.GERMANY.icon
            SupportedLanguage.FRENCH.value -> SupportedLanguage.FRENCH.icon
            SupportedLanguage.SPANISH.value -> SupportedLanguage.SPANISH.icon
            else -> SupportedLanguage.ENGLISH.icon
        }
    }
}