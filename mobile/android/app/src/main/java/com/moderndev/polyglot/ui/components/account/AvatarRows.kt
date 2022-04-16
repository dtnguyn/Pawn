package com.moderndev.polyglot.ui.components.account

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moderndev.polyglot.ui.theme.Grey

@Composable
fun AvatarRows(icons: List<String>, current: String, onClick: (icon: String) -> Unit){

    val resources = LocalContext.current.resources


    var i = 0
    while (i < icons.size) {
        Row(modifier = Modifier.fillMaxWidth()) {
            var j = i
            val end = j + 4
            while (j < end) {
                if (j < icons.size) {
                    val currentIndex = j
                    Card(
                        shape = CircleShape,
                        backgroundColor = if(current == icons[j]) Grey else Color.White,
                        modifier = Modifier
                            .clip(CircleShape)
                            .weight(1 / 4f)
                            .aspectRatio(1f)
                            .clickable {
                                Log.d("AvatarRows", "icon $currentIndex ${icons[j]}")
                                onClick(icons[currentIndex])
                            }
                    ) {
                        val resId = resources.getIdentifier(icons[currentIndex], "drawable", "com.moderndev.polyglot")
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = "avatar icon",
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .weight(1 / 4f)
                            .aspectRatio(1f)
                    )
                }
                j++
            }
            i += 4
        }
    }
}