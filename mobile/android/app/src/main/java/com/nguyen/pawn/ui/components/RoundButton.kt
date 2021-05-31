package com.nguyen.pawn.ui.components

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.R

@Composable
fun RoundButton(
    backgroundColor: androidx.compose.ui.graphics.Color,
    size: Dp,
    icon: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        backgroundColor = backgroundColor,

        ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "icon",
            modifier = Modifier
                .padding(7.dp)
        )
    }
}