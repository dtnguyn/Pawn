package com.nguyen.pawn.util

import androidx.compose.ui.graphics.Color
import com.nguyen.pawn.ui.theme.LightGreen
import com.nguyen.pawn.ui.theme.LightOrange
import com.nguyen.pawn.ui.theme.LightRed
import com.nguyen.pawn.ui.theme.Neon

object UtilFunction {

    fun generateColor(index: Int): Color {
        return when {
            index % 4 == 0 -> LightGreen
            index % 3 == 0 -> LightOrange
            index % 2 == 0 -> Neon
            else -> LightRed
        }
    }
}