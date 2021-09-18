package com.nguyen.polygot.util

import androidx.compose.ui.graphics.Color
import com.nguyen.polygot.R
import com.nguyen.polygot.ui.theme.Blue
import com.nguyen.polygot.ui.theme.DarkBlue
import com.nguyen.polygot.ui.theme.LightOrange
import com.nguyen.polygot.ui.theme.LightRed

enum class SupportedLanguage(val id: String, val flag: Int, val icon: Int, val backgroundColor: Color) {
    ENGLISH("en_US", R.drawable.english, R.drawable.big_ben, Blue),
    SPANISH("es",R.drawable.spain, R.drawable.bull, LightRed),
    FRENCH("fr", R.drawable.france, R.drawable.eiffel_tower, DarkBlue),
    GERMANY("de", R.drawable.germany, R.drawable.beer, LightOrange)
}