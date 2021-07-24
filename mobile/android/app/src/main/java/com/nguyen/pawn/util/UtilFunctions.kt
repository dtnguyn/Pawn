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
            SupportedLanguage.ENGLISH.id -> SupportedLanguage.ENGLISH.backgroundColor
            SupportedLanguage.GERMANY.id -> SupportedLanguage.GERMANY.backgroundColor
            SupportedLanguage.FRENCH.id -> SupportedLanguage.FRENCH.backgroundColor
            SupportedLanguage.SPANISH.id -> SupportedLanguage.SPANISH.backgroundColor
            else -> SupportedLanguage.ENGLISH.backgroundColor
        }
    }

    fun generateFlagForLanguage(language: String): Int {
        return when (language) {
            SupportedLanguage.ENGLISH.id -> SupportedLanguage.ENGLISH.flag
            SupportedLanguage.GERMANY.id -> SupportedLanguage.GERMANY.flag
            SupportedLanguage.FRENCH.id -> SupportedLanguage.FRENCH.flag
            SupportedLanguage.SPANISH.id -> SupportedLanguage.SPANISH.flag
            else -> SupportedLanguage.ENGLISH.flag
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
}