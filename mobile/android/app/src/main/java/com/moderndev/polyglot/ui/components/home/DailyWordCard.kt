package com.moderndev.polyglot.ui.components.home

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.moderndev.polyglot.ui.components.RoundButton
import com.moderndev.polyglot.ui.theme.ReallyRed
import com.moderndev.polyglot.ui.theme.Typography
import com.moderndev.polyglot.util.ShimmerAnimation
import com.moderndev.polyglot.R

private const val TAG = "DailyWordCard"

@Composable
fun DailyWordCard(
    isLoading: Boolean,
    word: String,
    definition: String,
    pronunciationSymbol: String? = null,
    pronunciationAudio: String? = null,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    Box(Modifier.padding(horizontal = 30.dp)) {
        Card(
            shape = RoundedCornerShape(60.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(60.dp))
                .height(250.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick),
            backgroundColor = ReallyRed
        ) {
            if (isLoading) {
                Column(Modifier.padding(25.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ShimmerAnimation(modifier = Modifier.width(200.dp).height(40.dp), shape = RoundedCornerShape(30.dp))
                        ShimmerAnimation(modifier = Modifier.width(45.dp).height(44.dp), shape = CircleShape)
                    }
                    Spacer(Modifier.padding(15.dp))
                    ShimmerAnimation(modifier = Modifier.width(250.dp).height(20.dp), shape = RoundedCornerShape(30.dp))
                    Spacer(Modifier.padding(5.dp))
                    ShimmerAnimation(modifier = Modifier.width(250.dp).height(20.dp), shape = RoundedCornerShape(30.dp))
                    Spacer(Modifier.padding(5.dp))
                    ShimmerAnimation(modifier = Modifier.width(150.dp).height(20.dp), shape = RoundedCornerShape(30.dp))
                }
            } else {
                Column(Modifier.padding(25.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(text = word, style = Typography.h1, color = Color.White)
                            Text(
                                text = pronunciationSymbol ?: "",
                                style = Typography.body2,
                                color = Color.White
                            )
                        }
                        RoundButton(
                            backgroundColor = Color.White,
                            size = 45.dp,
                            icon = R.drawable.speaker,
                            onClick = {
                                if(pronunciationAudio == null) {

                                } else {
                                    val audioUrl = if ("http" in pronunciationAudio) pronunciationAudio else "https:$pronunciationAudio"
                                    MediaPlayer.create(
                                        context,
                                        Uri.parse(audioUrl)
                                    ).start()
                                }
                            }
                        )
                    }
                    Spacer(Modifier.padding(5.dp))
                    Text(text = definition, style = Typography.body1, color = Color.White)
                }
            }

        }
    }

}