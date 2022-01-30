package com.nguyen.polyglot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nguyen.polyglot.ui.theme.DarkBlue
import com.nguyen.polyglot.ui.theme.LightGrey

@Composable
fun CircularLoadingBar() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(LightGrey.copy(0.6f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = DarkBlue
        )
    }
}