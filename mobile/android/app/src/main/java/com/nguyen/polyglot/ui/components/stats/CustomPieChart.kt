package com.nguyen.polyglot.ui.components.stats

import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import java.lang.Integer.min
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nguyen.polyglot.ui.theme.Typography
import kotlin.math.atan2
import kotlin.math.roundToInt

@Composable
fun CustomPieChart(
    title: String = "",
    subtitle: String = "",
    modifier: Modifier,
    progress: List<Float>,
    colors: List<Color>,
    isDonut: Boolean = false,
    isClickable: Boolean = false,
    percentColor: Color = Color.White,
    activePie: Int = -1
) {

    if (progress.isEmpty() || progress.size != colors.size) return

    val total = progress.sum()
    val proportions = progress.map {
        it * 100 / total
    }
    val angleProgress = proportions.map {
        360 * it / 100
    }

    val progressSize = mutableListOf<Float>()
    progressSize.add(angleProgress.first())

    for (x in 1 until angleProgress.size)
        progressSize.add(angleProgress[x] + progressSize[x - 1])

    var startAngle = 270f

    BoxWithConstraints(modifier = modifier) {

        val sideSize = constraints.maxWidth

        val pathPortion = remember {
            Animatable(initialValue = 0f)
        }
        LaunchedEffect(key1 = true) {
            pathPortion.animateTo(
                1f, animationSpec = tween(1000)
            )
        }


        Canvas(
            modifier = Modifier
                .width(constraints.maxWidth.dp)
                .height(constraints.maxHeight.dp)
                .clipToBounds()
//                .pointerInput(true) {
//
//                    if (!isDonut || !isClickable)
//                        return@pointerInput
//
//                    detectTapGestures {
//                        val clickedAngle = convertTouchEventPointToAngle(
//                            sideSize.toFloat(),
//                            sideSize.toFloat(),
//                            it.x,
//                            it.y
//                        )
//                        progressSize.forEachIndexed { index, item ->
//                            if (clickedAngle <= item) {
//                                if (activePie != index)
//                                    activePie = index
//
//                                return@detectTapGestures
//                            }
//                        }
//                    }
//                }
        ) {

            val canvasWidth = size.width

            angleProgress.forEachIndexed { index, arcProgress ->
                drawPie(
                    colors[index],
                    startAngle,
                    arcProgress * pathPortion.value,
                    Size(canvasWidth - 160f, canvasWidth - 160f),
                    topLeft = Offset(80f, 80f),
                    isDonut = isDonut,
                    isActive = activePie == index
                )
                startAngle += arcProgress
            }
        }
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = title,
                style = Typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = subtitle, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }

}

private fun DrawScope.drawPie(
    color: Color,
    startAngle: Float,
    arcProgress: Float,
    size: Size,
    topLeft: Offset,
    isDonut: Boolean = false,
    isActive: Boolean = false
): Path {

    return Path().apply {
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = arcProgress,
            useCenter = !isDonut,
            size = size,
            style = if (isDonut) Stroke(
                width = if (isActive) 150f else 100f,
            ) else Fill,
            topLeft = topLeft
        )
    }
}


private fun convertTouchEventPointToAngle(
    width: Float,
    height: Float,
    xPos: Float,
    yPos: Float
): Double {
    var x = xPos - (width * 0.5f)
    val y = yPos - (height * 0.5f)

    var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
    angle = if (angle < 0) angle + 360 else angle
    return angle
}
