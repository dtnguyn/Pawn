package com.moderndev.polyglot.util

import androidx.compose.ui.graphics.Color
import com.moderndev.polyglot.R
import com.moderndev.polyglot.ui.theme.Blue
import com.moderndev.polyglot.ui.theme.DarkBlue
import com.moderndev.polyglot.ui.theme.LightOrange
import com.moderndev.polyglot.ui.theme.LightRed

enum class SupportedLanguage(val id: String, val flag: Int, val icon: Int, val backgroundColor: Color) {
    ENGLISH("en_US", R.drawable.english, R.drawable.big_ben, Blue),
    SPANISH("es",R.drawable.spain, R.drawable.bull, LightRed),
    FRENCH("fr", R.drawable.france, R.drawable.eiffel_tower, DarkBlue),
    GERMANY("de", R.drawable.germany, R.drawable.beer, LightOrange)
}