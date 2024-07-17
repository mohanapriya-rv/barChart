package com.example.barchart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.barchart.ui.theme.BarChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    DrawLines()
                }
            }
        }
    }
}
//        drawLine(
//            start = Offset(0f, y = 0f),
//            end = Offset(canvasWidth , canvasHeight),
//            color = Color.Blue,
//            strokeWidth = 7f
//        )

@Composable
fun DrawLines() {
    Canvas(modifier = Modifier.fillMaxWidth()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val verticalpadding = 90f
        val horizontalPadding = 120f
        val lineCount = 10
        // vertical baseLine
        drawLine(
            start = Offset(
                verticalpadding,
                y = (canvasHeight / 2) - (horizontalPadding + verticalpadding)
            ),
            end = Offset(verticalpadding, canvasHeight - horizontalPadding),
            color = Color.Blue,
            strokeWidth = 7f
        )
        // horizontal baseLine
//        drawLine(
//            start = Offset(verticalpadding, y = canvasHeight - horizontalPadding),
//            end = Offset(canvasWidth - horizontalPadding, canvasHeight - horizontalPadding),
//            color = Color.Blue,
//            strokeWidth = 7f
//        )
        for (i in 0..<lineCount) {
            val loopCount = i + 1
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