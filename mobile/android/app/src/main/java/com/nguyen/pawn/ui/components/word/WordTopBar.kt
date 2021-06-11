package com.nguyen.pawn.ui.components.word

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.ReallyRed
import com.nguyen.pawn.ui.theme.Typography

@ExperimentalAnimationApi
@Composable
fun WordTopBar(lazyListState: LazyListState, word: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(ReallyRed)
            .padding(horizontal = 5.dp),
    ) {

        IconButton(modifier = Modifier.align(Alignment.CenterStart),onClick = onBackClick) {
            Image(
                painter = painterResource(id = R.drawable.back_32_white),
                contentDescription = "Back icon"
            )
        }
        AnimatedVisibility(
            visible = lazyListState.firstVisibleItemIndex >= 1,
            enter = slideInVertically(initialOffsetY = { 400 }) + expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically(targetOffsetY = { 400 }) + shrinkVertically() + fadeOut(),
            modifier = Modifier.align(
                Alignment.Center
            )
        ) {
            Text(
                text = word,
                style = Typography.h4,
                color = Color.White,

                )
        }

    }
}