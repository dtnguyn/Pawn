package com.nguyen.polyglot.ui.components.stats

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyen.polyglot.model.LanguageReport
import com.nguyen.polyglot.ui.theme.Blue
import com.nguyen.polyglot.ui.theme.Typography

@Composable
fun CustomBarChart(
    title: String,
    subtitle: String,
    reports: List<LanguageReport>,
    currentLanguageId: String,
    allSavedWordsCount: Int
) {


    LaunchedEffect(true) {
//       Log.d("CustomBarChart", "height ${}")
    }

    Column(
        Modifier
            .fillMaxWidth()
            .aspectRatio(5 / 6f)
            .background(Color.Red),
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
        BoxWithConstraints() {
            val width = maxWidth
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                Box(
                    modifier = Modifier
                        .width(width.times(0.056f))
                        .background(Color.Transparent)
                )

                (reports.find { it.languageId == currentLanguageId })?.wordTopicReports?.forEachIndexed { index, item ->

                    Column(
                        modifier = Modifier
                            .width(width.times(0.18f))
                            .align(Alignment.Bottom),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Text(
                            text = item.wordCount.toString(),
                            style = Typography.body1,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight((item.wordCount.toFloat() / reports[0].savedWordCount))
                                .background(Blue, RoundedCornerShape(10.dp))
                        )
                        if (item.wordCount == 0) Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(Blue)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(text = item.value, style = Typography.body1, fontSize = 14.sp)

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