package com.example.barchart

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.barchart.ui.theme.BarChartTheme
import com.example.barchart.ui.theme.HistoricalData
import com.example.barchart.ui.theme.internalValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarChartTheme {
                // A surface container using the 'background' color from the theme

                val h = HistoricalData(
                    "2000", internalValue(11000.00),
                    internalValue(11000.00), internalValue(11000.00), internalValue(11000.00)
                )
                val h2 = HistoricalData(
                    "2000", internalValue(0.00),
                    internalValue(1000.00), internalValue(11000.00), internalValue(51000.00)
                )
                val h3 = HistoricalData(
                    "2000", internalValue(4550.00),
                    internalValue(100.00), internalValue(1100.00), internalValue(0.0)
                )
                val h4 = HistoricalData(
                    "2000", internalValue(0.0),
                    internalValue(110087.00), internalValue(110087.00), internalValue(110087.0)
                )
                val dataList = listOf(h4)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .fillMaxHeight()
                ) {
                    DrawChart(dataList)
                }
            }
        }
    }
}


@Composable
fun DrawChart(dataList: List<HistoricalData>) {
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

        var barChartStartPosition = 100f
        var barChartEndPosition = 1f
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
        // bottomPadding for extra padding
        for (i in 0 until lineCount) {
            val loopCount = i + 1
            // lines drwaing from top,,sor percentage calulated in reverse
            val loopPercentage = (lineCount + 1 - loopCount) * 10
            drawContext.canvas.nativeCanvas.drawText(
                "$loopPercentage%", 30f, individualIntervalCount * loopCount, paint
            )
            Log.e("loopPercentage", loopPercentage.toString())

            Log.e(
                "priyaStart",
                startPosition.toString().plus(" ").plus(individualIntervalCount * loopCount)
            )
            Log.e(
                "priyaend",
                canvasWidth.toString().plus(" ").plus(individualIntervalCount * loopCount)
            )
            barChartEndPosition = individualIntervalCount * loopCount
            drawLine(
                start = Offset(
                    x = startPosition, y = individualIntervalCount * loopCount
                ), end = Offset(
                    canvasWidth, y = individualIntervalCount * loopCount
                ), color = Color.Blue, strokeWidth = 7f
            )

        }

        val rectWidthPx = (screenWidthPx / lineCount).toFloat()
        for (i in dataList.indices) {
            // 60 for initial space
            val barStartTopPosition = (rectWidthPx * (i) + 120).toFloat()

            val barTotalHeight = barChartEndPosition - barChartStartPosition
            val data = dataList[i]
            computeHeightPosition(data, barTotalHeight)
            var currentTop = barChartStartPosition


            val assets = listOf(data.equity, data.debt, data.others, data.commodity)
            assets.forEachIndexed { index, asset ->
                val top = barStartTopPosition

                val topLeftChildOffset = Offset(x = top, y = currentTop)
                val rectSize =
                    Size(width = rectWidthPx - horizontalBarPadding, height = asset.height)
                drawRect(
                    color = getColorForAsset(index), size = rectSize, topLeft = topLeftChildOffset
                )

                currentTop += asset.height
            }


            drawContext.canvas.nativeCanvas.drawText(
                "2000",
                barStartTopPosition.toFloat(),
                halfScreenHeightInPixels - bottomPadding / 2,
                paint
            )
        }

    }
}

fun getColorForAsset(index: Int): Color {
    return when (index) {
        0 -> Color.Blue
        1 -> Color.Gray
        2 -> Color.Green
        3 -> Color.Black
        4 -> Color.Cyan
        else -> Color.Magenta
    }
}

fun computeHeightPosition(data: HistoricalData, barTotalHeight: Float) {
    val totalAmount =
        (data.equity.amount + data.debt.amount + data.others.amount + data.commodity.amount)
    data.equity.height = getTotalHeight(data.equity.amount, totalAmount, barTotalHeight).toFloat()
    data.debt.height = getTotalHeight(data.debt.amount, totalAmount, barTotalHeight).toFloat()
    data.others.height = getTotalHeight(data.others.amount, totalAmount, barTotalHeight).toFloat()
    data.commodity.height =
        getTotalHeight(data.commodity.amount, totalAmount, barTotalHeight).toFloat()

}

fun getTotalHeight(amount: Double, totalAmount: Double, barTotalHeight: Float): Double {
    val equityPer = amount / totalAmount
    return equityPer * barTotalHeight
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