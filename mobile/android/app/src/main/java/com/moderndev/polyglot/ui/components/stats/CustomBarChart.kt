package com.moderndev.polyglot.ui.components.stats

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moderndev.polyglot.ui.theme.Blue
import com.moderndev.polyglot.ui.theme.Typography

@Composable
fun CustomBarChart(
    title: String,
    subtitle: String,
    values: List<Int>,
    labels: List<String>,
    maxValue: Int,
) {

    if (values.size != labels.size) return


    LaunchedEffect(true) {
//       Log.d("CustomBarChart", "height ${}")
    }

    Column(
        Modifier
            .fillMaxWidth()
            .aspectRatio(5 / 6f)
            .padding(bottom = 10.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = title,
            style = Typography.h6,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 10.dp)
        )
        Text(
            text = subtitle,
            style = Typography.body1,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 10.dp)
        )
        BoxWithConstraints(contentAlignment = Center) {
            val width = maxWidth
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                Box(
                    modifier = Modifier
                        .width(width.times(0.056f))
                        .background(Color.Transparent)
                )

                values.forEachIndexed { index, value ->
                    Log.d("CustomBarChart", "$value $maxValue " + if (maxValue == 0) 0.005f else (value / maxValue) * 0.9f)
                    Column(
                        modifier = Modifier
                            .width(width.times(0.18f))
                            .align(Alignment.Bottom),
                        horizontalAlignment = CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.padding(3.dp))
                        Column(
                            modifier = if (value == 0)
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f)
                            else Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = value.toString(),
                                style = Typography.body1,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(if (maxValue == 0) 0.005f else (value.toFloat() / maxValue) * 0.85f + 0.005f)
                                    .background(Blue, RoundedCornerShape(10.dp))
                            )
                        }
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(text = labels[index], style = Typography.body1, fontSize = 14.sp)

                    }
                    Box(
                        modifier = Modifier
                            .width(width.times(0.056f))
                            .background(Color.Transparent)
                    )
                }

            }
        }


    }
}