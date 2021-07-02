package com.nguyen.pawn.util

import androidx.compose.ui.graphics.Color
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.Blue
import com.nguyen.pawn.ui.theme.DarkBlue
import com.nguyen.pawn.ui.theme.LightOrange
import com.nguyen.pawn.ui.theme.LightRed

enum class SupportedLanguage(val value: String, val flag: Int, val icon: Int, val backgroundColor: Color) {
    ENGLISH("en_US", R.drawable.english, R.drawable.big_ben, Blue),
    SPANISH("es",R.drawable.spain, R.drawable.bull, LightRed),
    FRENCH("fr", R.drawable.france, R.drawable.eiffel_tower, DarkBlue),
    GERMANY("de", R.drawable.germany, R.drawable.beer, LightOrange)
}