package com.khue.composecanvas

import MarketChart
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khue.composecanvas.chart.StockChart
import com.khue.composecanvas.chart.market_chart.Candle
import com.khue.composecanvas.ui.theme.ComposeCanvasTheme
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCanvasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    MarketChartCompose()
                }
            }
        }
    }
}

@Composable
fun MarketChartCompose() {
    val candles = mutableListOf<Candle>()
    val context = LocalContext.current

    context.assets.open("quotes.txt").use {
        it.bufferedReader().forEachLine { line ->
            val splitStrings = line.split(" ")

            val year = splitStrings[0].substring(0, 4).toInt()
            val month = splitStrings[0].substring(4, 6).toInt()
            val day = splitStrings[0].substring(6, 8).toInt()
            val hour = splitStrings[1].substring(0, 2).toInt()
            val minute = splitStrings[1].substring(2, 4).toInt()

            val dateTime = LocalDateTime.of(year, month, day, hour, minute)
            val open = splitStrings[2].toFloat()
            val high = splitStrings[3].toFloat()
            val low = splitStrings[4].toFloat()
            val close = splitStrings[5].toFloat()

            candles.add(Candle(dateTime, open, close, high, low))
        }
        candles.sort()
    }
    MarketChart(candles)
}

@Composable
@OptIn(ExperimentalTextApi::class)
private fun StockChart() {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        val x = ((16 * 150)).pxToDp()
        val y = rememberScrollState()
        Card(shape = MaterialTheme.shapes.large, colors = CardDefaults.elevatedCardColors()) {
            Box(Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                StockChart(
                    modifier = Modifier
                        .horizontalScroll(y)
                        .width(x)
                        .height(300.dp),
                    infos = listOf(
                        249.75f to 4, 250.35f to 5, 249.81f to 6, 249.17f to 7, 250.61f to 8, 253.24f to 9, 252.9f to 10, 252.0f to 11,
                        250.856f to 12, 246.77f to 13, 250.39f to 14, 250.23f to 15, 250.01f to 16, 249.97f to 17, 249.89f to 18, 249.95f to 19
                    ),
                )
            }
        }
    }
}

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        // Creating a canvas and creating a triangular path
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)) {

            val rect = Rect(Offset.Zero, size)
            rect.left
            rect.top
            rect.right
            rect.bottom
            val trianglePath = Path().apply {
                moveTo(rect.topCenter.x, rect.topCenter.y)
                lineTo(rect.bottomRight.x, rect.bottomRight.y)
                lineTo(rect.bottomLeft.x, rect.bottomLeft.y)
                lineTo(500f, 150f)
                close()
            }

            drawPath(
                trianglePath,
                color = Color.Green,
            )
//            // Adding a path effect of rounded corners
//            drawIntoCanvas { canvas ->
//                canvas.drawOutline(
//                    outline = Outline.Generic(trianglePath),
//                    paint = Paint().apply {
//                        color = Color.Green
//                        pathEffect = PathEffect.cornerPathEffect(rect.maxDimension / 3)
//                    }
//                )
//            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeCanvasTheme {
        Greeting("Android")
    }
}