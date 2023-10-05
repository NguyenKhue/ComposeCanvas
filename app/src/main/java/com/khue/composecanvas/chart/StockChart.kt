package com.khue.composecanvas.chart


import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.round
import kotlin.math.roundToInt


@Composable
@ExperimentalTextApi
fun StockChart(
    modifier: Modifier = Modifier,
    infos: List<Pair<Float, Int>> = emptyList(),
    graphColor: Color = Color.Green
) {
    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val upperValue = remember(infos) {
        (infos.maxOfOrNull { it.first }?.plus(1))?.roundToInt() ?: 0
    }
    val lowerValue = remember(infos) {
        infos.minOfOrNull { it.first }?.toInt() ?: 0
    }

    val textMeasure = rememberTextMeasurer()

    Canvas(
        modifier = modifier,
    ) {
        val spacePerHour = 150f
        infos.indices.forEach { i ->
            val info = infos[i]
            val hour = info.second

            val hourTextMeasure = textMeasure.measure(
                text = hour.toString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                ),
            )
            Log.d("StockChart", "StockChart: $i")
            drawText(hourTextMeasure, topLeft = Offset(spacing + i * spacePerHour, size.height - 5))
        }

        val priceStep = (upperValue - lowerValue) / 5f
        (0..4).forEach { i ->
            val priceTextMeasure = textMeasure.measure(
                text = round(lowerValue + priceStep * i).toString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                ),
            )
            drawText(priceTextMeasure, topLeft = Offset(20f, size.height - spacing - i * size.height / 5f))
        }

        var lastX = 0f

        val strokePath = Path().apply {
            val height = size.height
            for (i in infos.indices) {
                val info = infos[i]
                val nextInfo = infos.getOrNull(i + 1) ?: infos.last()
                val leftRatio = (info.first - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.first - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height)
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height)
                if (i == 0) {
                    moveTo(x1, y1)
                }

                lastX = (x1 + x2) / 2f

                quadraticBezierTo(
                    x1, y1, lastX, (y1 + y2) / 2f
                )
            }
        }

        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }

        drawContext.canvas.nativeCanvas.inClip(
            left = 0f, top = 0f, right = size.width, bottom = size.height
        ) {
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        transparentGraphColor,
                        Color.Transparent
                    ),
                    endY = size.height - spacing
                )
            )
            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}
public inline fun Canvas.inClip(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    block: () -> Unit,
) {
    val clipRestoreCount = save()
    clipRect(left, top, right, bottom)
    block()
    restoreToCount(clipRestoreCount)
}
