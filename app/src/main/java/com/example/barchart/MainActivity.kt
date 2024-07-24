package com.example.barchart

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltipBox
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.barchart.ui.theme.BarChartTheme
import com.example.barchart.ui.theme.HistoricalData
import com.example.barchart.ui.theme.internalValue
import com.fundsindia.customComponent.TickOrientation
import com.fundsindia.customComponent.ToolTipShape
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarChartTheme {
                // A surface container using the 'background' color from the theme

                val h = HistoricalData(
                    "2000",
                    internalValue(11000.00),
                    internalValue(11000.00),
                    internalValue(11000.00),
                    internalValue(11000.00)
                )
                val h2 = HistoricalData(
                    "2000",
                    internalValue(0.00),
                    internalValue(100.00),
                    internalValue(11000000.00),
                    internalValue(51000.00)
                )
                val h3 = HistoricalData(
                    "2000",
                    internalValue(4550.00),
                    internalValue(100.00),
                    internalValue(1100.00),
                    internalValue(0.0)
                )
                val h4 = HistoricalData(
                    "2000",
                    internalValue(0.0),
                    internalValue(110087.00),
                    internalValue(110087.00),
                    internalValue(110087.0)
                )

                val dataList = listOf(h, h2, h3, h4, h, h2, h3, h4, h, h2, h3, h4)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .fillMaxHeight()
                ) {
                    DrawChartNew(dataList)
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipWithRichContent(offset: Offset, text: String) {
    Log.e("priyaRichContent", offset.toString())

//    Popup(
//        offset = IntOffset(offset.x.toInt(), offset.y.toInt()),
//        properties = PopupProperties(focusable = false)
//    ) {
//        RichTooltipBox(
//            text = { /* Custom text content */ },
//            modifier = Modifier.width(200.dp).height(200.dp),
//            shape = TooltipDefaults.richTooltipContainerShape,
//            colors = RichTooltipColors(
//                containerColor = Color.Black,
//                contentColor = Color.Magenta,
//                actionContentColor = Color.Gray,
//                titleContentColor = Color.Black
//            )
//        ) {
//            Column(
//                modifier = Modifier.wrapContentSize(),
//                verticalArrangement = Arrangement.Top
//            ) {
//                Text(text = "Equity ₹25,000 (50%)")
//                Text(text = "Debt ₹25,000 (10%)")
//                Text(text = "Gold ₹2,000 (35%)")
//                Text(text = "Others ₹500 (5%)")
//            }
//        }
//    }
    Popup(
        offset = IntOffset(offset.x.roundToInt(), offset.y.roundToInt()),
        properties = PopupProperties(focusable = false)
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black, shape = RoundedCornerShape(10.dp))
                .padding(10.dp)
                .width(190.dp)
                .height(80.dp)
        ) {
            Column(
                modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Equity ₹25,000 (50%)", color = Color.Magenta)
                Text(text = "Debt ₹25,000 (10%)", color = Color.Magenta)
                Text(text = "Gold ₹2,000 (35%)", color = Color.Magenta)
                Text(text = "Others ₹500 (5%)", color = Color.Magenta)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawChartNew(dataList: List<HistoricalData>) {
    val showToolTip = remember { mutableStateOf(false) }

    DrawChart(dataList) {
        showToolTip.value = true
    }
    if (showToolTip.value) {
        RichTooltipBox(
            text = {

            },
            modifier = Modifier
                .height(200.dp)
                .width(200.dp),
            shape = TooltipDefaults.richTooltipContainerShape,
            colors = RichTooltipColors(
                containerColor = Color.Black,
                contentColor = Color.Magenta,
                actionContentColor = Color.Gray,
                titleContentColor = Color.Black
            )


        ) {
            val shape = ToolTipShape(
                cornerRadiusDp = 10.dp, tickHeight = 15.dp, tickOrientation = TickOrientation.START
            )
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top
            ) {
                Text(text = "priya")
                Text(text = "ramasamy")
                Text(text = "priya")
            }
        }
    }
}

@Composable
fun DrawChart(dataList: List<HistoricalData>, callback: () -> Unit) {
    val scrollState = rememberScrollState()
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenHeightPx = displayMetrics.heightPixels
    val density = displayMetrics.density
    val screenHeightDp = screenHeightPx / density
    val halfScreenHeightDp = screenHeightDp / 2
    val bottomPadding = 120f

    val halfScreenHeightInPixels = (halfScreenHeightDp * density)
    val screenWidthPx = displayMetrics.widthPixels
    val screenWidthPxForBar = displayMetrics.widthPixels - 100
    val screenWidthDp = screenWidthPx / density
    val halfScreenWidthDp = screenWidthDp / 2
    val rectPositions = remember { mutableStateListOf<Pair<Float, String>>() }
    val showToolTip = remember { mutableStateOf(false) }
    val startPosition = 120f
    var toolTipOffset = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    if (showToolTip.value) {
        TooltipWithArrow(
            offset = toolTipOffset.value,
            text = "Equity ₹25,000 (50%)\nDebt ₹25,000 (10%)\nGold ₹2,000 (35%)\nOthers ₹500 (5%)"
        )
    }
    val lineCount = 10
    val halfScreenHighPXAfterPadding = halfScreenHeightInPixels - bottomPadding

    val individualIntervalCount = halfScreenHighPXAfterPadding / 10
    Canvas(
        modifier = Modifier
            .background(Color.Magenta)
            .width(45.dp)
            .height(halfScreenHeightDp.dp)

    ) {
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 40f
            color = android.graphics.Color.BLACK
        }
        drawLine(
            start = Offset(startPosition, y = 0f),
            end = Offset(startPosition, halfScreenHeightInPixels - bottomPadding),
            color = Color.Black,
            strokeWidth = 7f
        )
        for (i in 0 until lineCount) {
            val loopCount = i + 1
            val loopPercentage = (lineCount + 1 - loopCount) * 10
            drawContext.canvas.nativeCanvas.drawText(
                "$loopPercentage%", 30f, individualIntervalCount * loopCount, paint
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 45.dp)
            .height(halfScreenHeightDp.dp)
            .horizontalScroll(scrollState)
    ) {
        Canvas(modifier = Modifier
            .width((dataList.size * (screenWidthPxForBar / 5)).toFloat().dp)
            .height(halfScreenHeightDp.dp)
            .background(Color.Yellow)
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    for ((index, value) in rectPositions.withIndex()) {
                        val offset = tapOffset.x
                        if (offset in (value.first)..(value.first + 48)) {
                            Log.e("priya", tapOffset.toString())
                            toolTipOffset.value = tapOffset
                            showToolTip.value = true
                            break
                        } else {
                            showToolTip.value = false
                        }
                    }
                }
            }) {

            val canvasWidth = size.width

            for (i in 0 until lineCount) {
                val loopCount = i + 1
                drawLine(
                    start = Offset(x = 0f, y = individualIntervalCount * loopCount),
                    end = Offset(canvasWidth, y = individualIntervalCount * loopCount),
                    color = Color.Blue,
                    strokeWidth = 7f
                )
            }

            val barChartStartPosition = 100f
            val barChartEndPosition = halfScreenHeightInPixels - bottomPadding
            val barTotalHeight = barChartEndPosition - barChartStartPosition
            val rectWidthPx = (screenWidthPx / 10).toFloat()
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 40f
                color = android.graphics.Color.BLACK
            }
            for (i in dataList.indices) {
                val barStartTopPosition = ((rectWidthPx + 50f) * (i) + 50)
                val data = dataList[i]
                computeHeightPosition(data, barTotalHeight)
                var currentTop = barChartStartPosition
                val assets = listOf(data.equity, data.debt, data.others, data.commodity)
                assets.forEachIndexed { index, asset ->
                    val topLeftChildOffset = Offset(x = barStartTopPosition, y = currentTop)
                    val rectSize = Size(width = rectWidthPx - 60f, height = asset.height)
                    drawRect(
                        color = getColorForAsset(index),
                        size = rectSize,
                        topLeft = topLeftChildOffset
                    )
                    currentTop += asset.height
                }
                rectPositions.add(Pair(barStartTopPosition, data.year))

                drawContext.canvas.nativeCanvas.drawText(
                    data.year,
                    barStartTopPosition.toFloat(),
                    halfScreenHeightInPixels - bottomPadding / 2,
                    paint
                )
            }
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

fun dpToPx(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    )
}


@Composable
fun TooltipWithArrow(offset: Offset, text: String) {
    val formattedOffset = dpToPx(LocalContext.current, 350F)
    Log.e("priyaLocalOffset", offset.toString())
    Log.e("priyaformattedOffset", formattedOffset.toString())
    Popup(
        offset = IntOffset(
            offset.x.roundToInt() - 400, offset.y.roundToInt() - 200
        ), properties = PopupProperties(focusable = false)
    ) {
        TooltipWithArrowAndContent(offset)
    }
}


@Composable
fun TooltipArrow() {
    Canvas(
        modifier = Modifier
            .size(200.dp, 300.dp)
            .background(Color.Green)
    ) {
        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width / 2, size.height / 2)
            lineTo(0f, 0f)
            close()
        }
        drawPath(path, color = Color.White, style = Fill)
        drawPath(path, color = Color.Blue, style = Stroke(width = 2f))
    }
}

@Composable
fun TooltipContent(text: String) {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .widthIn(max = 200.dp)
            .border(2.dp, Color.Blue, RoundedCornerShape(8.dp))
    ) {
        text.split("\n").forEach { line ->
            Text(text = line, fontSize = 14.sp, color = Color.Black)
        }
    }
}

@Composable
fun TooltipWithArrowAndContent(offset: Offset) {
    Canvas(
        modifier = Modifier
            .width(150.dp)
            .height(80.dp)
    ) {
        val cornerRadius = 16.dp.toPx()
        val arrowWidth = 20.dp.toPx()
        val arrowHeight = 10.dp.toPx()

        val path = Path().apply {
            // Start at the top-left corner
            moveTo(cornerRadius, 0f)
            lineTo(size.width - cornerRadius, 0f)
            arcTo(
                rect = Rect(
                    size.width - cornerRadius * 2, 0f, size.width, cornerRadius * 2
                ), startAngleDegrees = -90f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            lineTo(size.width, size.height / 2)
            lineTo(size.width + arrowWidth, size.height / 2)
            lineTo(size.width, size.height / 2 + arrowHeight)

            lineTo(size.width, size.height - cornerRadius)
            arcTo(
                rect = Rect(
                    size.width - cornerRadius * 2,
                    size.height - cornerRadius * 2,
                    size.width,
                    size.height
                ), startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
            )

            lineTo(cornerRadius, size.height)
            arcTo(
                rect = Rect(
                    0f, size.height - cornerRadius * 2, cornerRadius * 2, size.height
                ), startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            lineTo(0f, cornerRadius)
            arcTo(
                rect = Rect(
                    0f, 0f, cornerRadius * 2, cornerRadius * 2
                ), startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            close()
        }

        drawPath(path, color = Color.White)
        drawPath(path, color = Color.Blue, style = Stroke(width = 4f))

        // Draw text content inside the tooltip
        drawContext.canvas.nativeCanvas.apply {
            val paint = android.graphics.Paint().apply {
                textSize = 14.sp.toPx()
                color = android.graphics.Color.BLACK
            }
            val textX = 16.dp.toPx()
            val textY = 24.dp.toPx()
            drawText("Equity ₹25,000 (50%)", textX, textY, paint)
            drawText("Debt ₹25,000 (10%)", textX, textY + 20.dp.toPx(), paint)
            drawText("Gold ₹2,000 (35%)", textX, textY + 40.dp.toPx(), paint)
            drawText("Others ₹500 (5%)", textX, textY + 60.dp.toPx(), paint)
        }
    }
}