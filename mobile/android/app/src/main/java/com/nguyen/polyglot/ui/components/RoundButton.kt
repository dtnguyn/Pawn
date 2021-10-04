package com.nguyen.polyglot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundButton(
    backgroundColor: androidx.compose.ui.graphics.Color,
    size: Dp,
    icon: Int,
    padding: Dp = 7.dp,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable(
                onClick = onClick
            ),
        backgroundColor = backgroundColor,
        elevation = 100.dp,
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "icon",
            modifier = Modifier.padding(padding)
        )

    }
}