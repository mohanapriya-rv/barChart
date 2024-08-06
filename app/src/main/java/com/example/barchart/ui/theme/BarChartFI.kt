package com.example.barchart.ui.theme


import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.res.ResourcesCompat
import com.example.barchart.R
import kotlin.math.roundToInt


@Composable
fun BarChartFI(dataList: List<FISegmentBarChartModel>, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .fillMaxHeight()
    ) {
        DrawChartView(dataList, context)
    }
}


@Composable
fun DrawChartView(dataList: List<FISegmentBarChartModel>, context: Context) {
    val scrollState = rememberScrollState()
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenHeightPx = displayMetrics.heightPixels
    val density = displayMetrics.density
    val screenHeightDp = screenHeightPx / density
    val halfScreenHeightDp = screenHeightDp / 2
    val bottomPadding = 150f

    val halfScreenHeightInPixels = (halfScreenHeightDp * density)
    val screenWidthPx = displayMetrics.widthPixels
    val screenWidthPxForBar = displayMetrics.widthPixels - 330f
    val rectPositions = remember { mutableStateListOf<Pair<Float, String>>() }
    val showToolTip = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableIntStateOf(-1) }

    val startPosition = 120f
    val arrowStartPosition = 70f
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
    val yearTextPosition = halfScreenHeightInPixels - bottomPadding + 50F
    val endYearPosition = halfScreenHeightInPixels - 50f

    val individualIntervalCount = halfScreenHighPXAfterPadding / 10
    val semiBoldPainter = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 40f
        color = context.getColor(R.color.purple_200)
    }

    val regularPainter = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 40f
        color = context.getColor(R.color.purple_200)
    }

    val actualWidth = displayMetrics.widthPixels
    val rectWidthPx = 80f
    // since intial offset of bar's starting from 200,so adding 208
    var totalWidthInPixel = (108f * dataList.size) + 208
    if (totalWidthInPixel < actualWidth) {
        totalWidthInPixel = actualWidth.toFloat()
    }


    val screenWidthInDp = totalWidthInPixel / density
    Log.e("priyadataList", dataList.size.toString())

    Log.e("priyaTotalPixel", totalWidthInPixel.toString())
    Log.e("priyaTotalWidth", screenWidthInDp.toString())
    val screenWidthEndPadding = totalWidthInPixel
    val modifier = if (totalWidthInPixel > actualWidth) {
        Modifier
            .height(halfScreenHeightDp.dp)
            .width(screenWidthEndPadding.dp)
            .padding(start = 50.dp)
            .horizontalScroll(rememberScrollState())
    } else {
        Modifier
            .height(halfScreenHeightDp.dp)
            .width(screenWidthEndPadding.dp)
            .padding(start = 50.dp)
    }
    Canvas(
        modifier = Modifier
            .width(45.dp)
            .height(halfScreenHeightDp.dp)

    ) {
        drawLine(
            start = Offset(startPosition, y = 0f),
            end = Offset(startPosition, halfScreenHeightInPixels - bottomPadding),
            color = Color.Magenta,
            strokeWidth = 3f
        )
        for (i in 0 until lineCount) {
            val loopCount = i + 1
            val loopPercentage = (lineCount + 1 - loopCount) * 10
            drawContext.canvas.nativeCanvas.drawText(
                "$loopPercentage%", 30f, individualIntervalCount * loopCount, semiBoldPainter
            )
        }
        // Draw a circle with an arrow
        val circleCenter = Offset(
            x = arrowStartPosition, y = halfScreenHeightInPixels - bottomPadding + 50f
        ) // Adjust the y position as needed
        val circleRadius = 20f


        drawCircle(
            color = Color(context.getColor(R.color.purple_500)), // Change to desired color
            radius = circleRadius, center = circleCenter
        )

        val arrowWidth = circleRadius / 4
        val arrowLineLength = circleRadius / 2
        // Draw the top line of the arrow

        drawLine(
            color = Color.White,
            start = Offset(circleCenter.x - arrowWidth, circleCenter.y),
            end = Offset(circleCenter.x + arrowWidth, circleCenter.y - arrowLineLength),
            strokeWidth = 4f
        )

        // Draw the bottom line of the arrow
        drawLine(
            color = Color.White,
            start = Offset(circleCenter.x + arrowWidth, circleCenter.y + arrowLineLength),
            end = Offset(circleCenter.x - arrowWidth, circleCenter.y),
            strokeWidth = 4f
        )
    }

    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier
            .height(halfScreenHeightDp.dp)
            .width(screenWidthEndPadding.dp)
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    for ((index, value) in rectPositions.withIndex()) {
                        val offset = tapOffset.x
                        if (offset in (value.first)..(value.first + 48)) {
                            toolTipOffset.value = tapOffset
                            selectedIndex.value = index
                            showToolTip.value = true
                            break
                        } else {
                            selectedIndex.value = -1
                            showToolTip.value = false
                        }
                    }
                }
            }) {

            for (i in 0 until lineCount) {
                val loopCount = i + 1
                drawLine(
                    start = Offset(x = 0f, y = individualIntervalCount * loopCount),
                    end = Offset(
                        totalWidthInPixel, y = individualIntervalCount * loopCount
                    ),
                    color = Color(context.getColor(R.color.black)),
                    strokeWidth = 3f
                )
            }

            val barChartStartPosition = 100f
            val barChartEndPosition = halfScreenHeightInPixels - bottomPadding
            val barTotalHeight = barChartEndPosition - barChartStartPosition
            val rectWidthPx = 120f

            for (i in dataList.indices) {
                val barStartTopPosition = (rectWidthPx * (i + 1))
                Log.e("priyastart", barStartTopPosition.toString())
                val data = dataList[i]
                computeHeightPosition(data, barTotalHeight)
                var currentTop = individualIntervalCount
                val asset = dataList[i].segments
                asset.forEachIndexed { index, asset ->
                    val topLeftChildOffset = Offset(x = barStartTopPosition, y = currentTop)
                    val rectSize = Size(width = rectWidthPx - 80f, height = asset.height)
                    drawRect(
                        color = asset.color, size = rectSize, topLeft = topLeftChildOffset
                    )
                    if (asset.height > 0.0) {
                        drawRoundRect(
                            color = asset.color, topLeft = Offset(
                                x = barStartTopPosition, y = currentTop - 15f
                            ),
                            size = Size(rectWidthPx - 80f, 30f), cornerRadius = CornerRadius(
                                rectWidthPx, rectWidthPx
                            )
                        )
                    }
                    Log.e("priyaoffset", topLeftChildOffset.toString())

                    currentTop += asset.height
                }
                rectPositions.add(Pair(barStartTopPosition, dataList[i].title))

                drawLeftAlignedText(
                    dataList[i].title,
                    barStartTopPosition,
                    yearTextPosition,
                    bottomPadding,
                    textColor = context.getColor(R.color.purple_200),
                    typeFace = Typeface.DEFAULT
                )
            }

            drawEndAlignedText(
                text = "* as on Dec 31st for each year",
                screenWidthPxForBar = screenWidthPxForBar,
                halfScreenHeightInPixels = endYearPosition,
                bottomPadding = bottomPadding,
                textColor = context.getColor(R.color.purple_200)
            )
        }
    }
}


fun computeHeightPosition(data: FISegmentBarChartModel, barTotalHeight: Float) {

    var totalAmount = 0.0
    data.segments.forEach {
        totalAmount += it.value
    }
    data.segments.forEach { individualSegment_ ->
        individualSegment_.height =
            getTotalHeight(individualSegment_.value, totalAmount, barTotalHeight).toFloat()
    }
}

fun getTotalHeight(amount: Double, totalAmount: Double, barTotalHeight: Float): Double {
    val equityPer = amount / totalAmount
    return equityPer * barTotalHeight
}

fun dpToPx(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    )
}


@Composable
fun TooltipWithArrow(offset: Offset, text: String) {
    val formattedOffset = dpToPx(LocalContext.current, 350F)
    Popup(
        offset = IntOffset(
            offset.x.roundToInt(), offset.y.roundToInt()
        ), properties = PopupProperties(focusable = false)
    ) {
        TooltipWithArrowAndContent(offset)
    }
}


@Composable
fun TooltipWithArrowAndContent(offset: Offset) {
    Log.e("priya", offset.toString())
    Canvas(
        modifier = Modifier
            .background(Color.Magenta)
            .size(200.dp, 200.dp)
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

fun DrawScope.drawEndAlignedText(
    text: String,
    screenWidthPxForBar: Float,
    halfScreenHeightInPixels: Float,
    bottomPadding: Float,
    textSizes: TextUnit = 12.sp, // Adjust the text size as needed
    textColor: Int

) {
    val paint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = textSizes.toPx()
        textAlign = android.graphics.Paint.Align.RIGHT
        color = textColor
    }

    drawContext.canvas.nativeCanvas.drawText(
        text, screenWidthPxForBar - 16f, // Small padding from the end
        halfScreenHeightInPixels, paint
    )
}

fun DrawScope.drawLeftAlignedText(
    text: String,
    screenWidthPxForBar: Float,
    halfScreenHeightInPixels: Float,
    bottomPadding: Float,
    textSizes: TextUnit = 12.sp, // Adjust the text size as needed
    textColor: Int,
    typeFace: Typeface,
) {

    val paint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = textSizes.toPx()
        textAlign = android.graphics.Paint.Align.LEFT
        color = textColor
        typeface = typeFace
    }

    drawContext.canvas.nativeCanvas.drawText(
        text, screenWidthPxForBar - 16f, // Small padding from the end
        halfScreenHeightInPixels, paint
    )
}