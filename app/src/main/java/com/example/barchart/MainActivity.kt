package com.example.barchart

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.barchart.ui.theme.BarChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarChartTheme {
                // A surface container using the 'background' color from the theme


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .fillMaxHeight()
                ) {
                    DrawChart()
                }
            }
        }
    }
}


@Composable
fun DrawChart() {
    val scrollState = rememberScrollState()
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenHeightPx = displayMetrics.heightPixels
    val density = displayMetrics.density
    val screenHeightDp = screenHeightPx / density
    val halfScreenHeightDp = screenHeightDp / 2
    val bottomPadding = 120f

    val halfScreenHeightInPixels = (halfScreenHeightDp * density)
    val screenWidthPx = displayMetrics.widthPixels
    val screenWidthDp = screenWidthPx / density
    val halfScreenWidthDp = screenWidthDp / 2
    val halfScreenWidthInPixels = halfScreenWidthDp * density
    Canvas(
        modifier = Modifier
            .background(Color.Yellow)
            .height(halfScreenHeightDp.dp)
            .fillMaxWidth()
    ) {
        val horizontalPadding = 120f
        val horizontalBarPadding = 60f
        val startPosition = 120f
        val canvasWidth = size.width
        val lineCount = 10

        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 40f
            color = android.graphics.Color.BLACK
        }

        // Draw lines
        drawLine(
            start = Offset(
                startPosition, y = 0f
            ),
            end = Offset(startPosition, halfScreenHeightInPixels - bottomPadding),
            color = Color.Blue,
            strokeWidth = 7f
        )


        val halfScreenHighPXAfterPadding = halfScreenHeightInPixels - bottomPadding
        val individualIntervalCount = halfScreenHighPXAfterPadding / 10
        Log.e("priyaindividualIntervalCount", individualIntervalCount.toString())

        // bottomPadding for extra padding
        for (i in 0 until lineCount) {
            val loopCount = i + 1
            // lines drwaing from top,,sor percentage calulated in reverse
            val loopPercentage = (lineCount + 1 - loopCount) * 10
            drawContext.canvas.nativeCanvas.drawText(
                "$loopPercentage%",
                30f,
                individualIntervalCount * loopCount,
                paint
            )
            Log.e("loopPercentage", loopPercentage.toString())

            Log.e(
                "priyaStart",
                startPosition.toString().plus(" ")
                    .plus(individualIntervalCount * loopCount)
            )
            Log.e(
                "priyaend",
                canvasWidth.toString().plus(" ")
                    .plus(individualIntervalCount * loopCount)
            )
            drawLine(
                start = Offset(
                    x = startPosition,
                    y = individualIntervalCount * loopCount
                ),
                end = Offset(
                    canvasWidth,
                    y = individualIntervalCount * loopCount
                ), color = Color.Blue, strokeWidth = 7f
            )

        }


        val rectWidthPx = screenWidthPx / lineCount
        val rectHeightPx = screenHeightPx / lineCount
        var barHeightStartPadding = 50f
        for (i in 1 until lineCount) {
            // 60 for intial space
            val barStartTopPosition = (rectWidthPx * (i) + 60)
            val topLeft =
                Offset(x = barStartTopPosition.toFloat(), y = barHeightStartPadding)

            Log.e("startXPostion", barStartTopPosition.toString())
            Log.e("startYPostion", barHeightStartPadding.toString())
            val rectSize = Size(
                width = rectWidthPx.toFloat() - horizontalBarPadding,
                height = halfScreenHeightInPixels - barHeightStartPadding - bottomPadding
            )
            drawRect(
                color = Color.White,
                size = rectSize,
                topLeft = topLeft
            )
            for (i in 1 until 4) {
                val leftStartChildPadding = barHeightStartPadding * i
                val topLeftOffSetPoint =
                    Offset(x = barStartTopPosition.toFloat(), y = leftStartChildPadding)
                Log.e("visaAcutalHeighr", halfScreenHeightInPixels.toString())
                Log.e("visa", leftStartChildPadding.toString())
                Log.e(
                    "visaheight",
                    (halfScreenHeightInPixels - barHeightStartPadding - bottomPadding).toString()
                )

                /**
                 *  total heights for each bar needs to be subtracted from existing bar
                 *  barheight =halfScreenHeightInPixels - barHeightStartPadding - bottomPadding
                 *  leftStartChildPadding -> child's bar end position needs to be end postion of parent
                 */


                val rectSize = Size(
                    width = rectWidthPx.toFloat() - horizontalBarPadding,
                    height = ((halfScreenHeightInPixels - barHeightStartPadding - bottomPadding) - leftStartChildPadding)
                )
                var color = Color.Magenta
                if (i == 1) {
                    color = Color.Blue
                } else if (i == 2) {
                    color = Color.Red
                } else if (i == 3) {
                    color = Color.Green
                } else if (i == 4) {
                    color = Color.Magenta
                }
                Log.e("visarectsize", rectSize.toString())
                Log.e("visarecttopleft", topLeftOffSetPoint.toString().plus("  ").plus(i))

                drawRect(
                    color = color,
                    size = rectSize,
                    topLeft = topLeftOffSetPoint
                )
                // Draw cap for the inner rectangle

            }
            // halfScreenHeightInPixels - bottomPadding / 2, to place the bottom of view

            drawContext.canvas.nativeCanvas.drawText(
                "2000",
                barStartTopPosition.toFloat(),
                halfScreenHeightInPixels - bottomPadding / 2,
                paint
            )
        }

    }


//        // Draw rectangles (bars)
//        for (i in 1 until 15) {
//            var barStartPosition = verticalPadding + (i * 200)
//            if (i != 1) {
//                barStartPosition += 80f
//            }
//            val topLeft = Offset(
//                x = barStartPosition, // Center horizontally
//                y = canvasHeight - horizontalPadding * 10 - ((canvasHeight / 2) - 20f) // Positioned relative to canvas height
//            )
//
//            Log.e("priya", topLeft.toString())
//
//            drawRect(
//                Color.Magenta, size = Size(
//                    width = rectWidth - horizontalPadding, height = (canvasHeight / 2) - 20f
//                ), topLeft = topLeft
//            )
//        }
}


@Composable
fun DrawRect() {
    val scrollState = rememberScrollState()

    Canvas(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .width(10000.dp)
            .height(600.dp)
    ) {
        val rectWidth = 200f
        val horizontalPadding = 120f
        val verticalPadding = 1f
        val canvasHeight = size.height

        for (i in 1..<15) {
            var barStartPosition = verticalPadding + (i * 200)
            if (i != 1) {
                barStartPosition += 80f
            }
            val topLeft = Offset(
                x = barStartPosition, // Center horizontally
                y = canvasHeight - horizontalPadding * 10// Bottom of the screen
            )

            drawRect(
                Color.Magenta, size = Size(
                    width = rectWidth - horizontalPadding, height = (size.height / 2) - 20f
                ), topLeft = topLeft
            )
        }
    }
}

@Composable
fun DrawLines() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val verticalpadding = 150f
        val horizontalPadding = 120f
        val lineCount = 10

        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 40f
            color = android.graphics.Color.BLACK

        }
        drawLine(
            start = Offset(
                verticalpadding, y = (canvasHeight / 2) - (horizontalPadding + verticalpadding)
            ),
            end = Offset(verticalpadding, canvasHeight - horizontalPadding),
            color = Color.Blue,
            strokeWidth = 7f
        )
        for (i in 0..<lineCount) {
            val loopCount = i + 1
            val loopPercentage = loopCount * 10
            drawContext.canvas.nativeCanvas.drawText(
                "$loopPercentage%", 50f, canvasHeight - horizontalPadding * loopCount, paint
            )

            drawLine(
                start = Offset(
                    x = verticalpadding, y = canvasHeight - horizontalPadding * loopCount
                ), end = Offset(
                    canvasWidth - horizontalPadding,
                    y = canvasHeight - horizontalPadding * loopCount
                ), color = Color.Blue, strokeWidth = 7f
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BarChartTheme {

    }
}