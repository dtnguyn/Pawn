package com.nguyen.pawn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundedSquareButton(backgroundColor: Color, icon: Int, size: Dp = 56.dp, onClick: () -> Unit) {
    Button(
        modifier= Modifier.size(size),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor),
        onClick = onClick
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "button icon",
            modifier = Modifier
                .align(Alignment.CenterVertically)

        )
    }

}