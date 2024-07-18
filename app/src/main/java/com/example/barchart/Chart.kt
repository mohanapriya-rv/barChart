//package com.example.barchart
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            Surface(
//                modifier = Modifier.fillMaxSize(),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                val data = listOf(
//                    listOf(30f, 20f, 50f),
//                    listOf(25f, 25f, 50f),
//                    listOf(20f, 30f, 50f),
//                    listOf(15f, 35f, 50f),
//                    listOf(10f, 40f, 50f),
//                    listOf(5f, 45f, 50f)
//                )
//                HistoricalAssetAllocationChart(data)
//            }
//        }
//    }
//}
//
//@Composable
//fun HistoricalAssetAllocationChart(data: List<List<Float>>) {
//    val colors = listOf(Color.Blue, Color.Cyan, Color.DarkGray)
//    val years = listOf("2019", "2020", "2021", "2022", "2023", "2024")
//
//    Column(
//        modifier = Modifier.padding(16.dp)
//    ) {
//        Text(
//            text = "Historical Asset Allocation",
//            fontSize = 20.sp,
//            color = Color.Black
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column(
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.End,
//                modifier = Modifier.fillMaxHeight()
//            ) {
//                for (i in 10 downTo 1) {
//                    Text(text = "${i * 10}%", fontSize = 12.sp)
//                }
//                Spacer(modifier = Modifier.height(20.dp))
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                data.forEachIndexed { index, allocation ->
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Canvas(
//                            modifier = Modifier
//                                .height(200.dp)
//                                .width(40.dp)
//                        ) {
//                            drawBar(allocation, colors)
//                            drawHorizontalLines()
//                        }
//                        Text(text = years[index])
//                    }
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = "* as on Dec 31st for each year",
//            fontSize = 12.sp,
//            color = Color.Gray
//        )
//    }
//}
//
//fun DrawScope.drawBar(allocation: List<Float>, colors: List<Color>) {
//    var currentHeight = size.height
//    val capHeight = 4.dp.toPx()
//    allocation.forEachIndexed { index, value ->
//        val barHeight = size.height * (value / 100)
//        drawRect(
//            color = colors[index],
//            topLeft = androidx.compose.ui.geometry.Offset(0f, currentHeight - barHeight),
//            size = androidx.compose.ui.geometry.Size(size.width, barHeight)
//        )
//        drawRect(
//            color = Color.White,
//            topLeft = androidx.compose.ui.geometry.Offset(0f, currentHeight - barHeight - capHeight),
//            size = androidx.compose.ui.geometry.Size(size.width, capHeight)
//        )
//        currentHeight -= barHeight
//    }
//}
//
//fun DrawScope.drawHorizontalLines() {
//    val lineColor = Color.LightGray
//    val paint = androidx.compose.ui.graphics.Paint().apply {
//        color = lineColor
//        strokeWidth = 1f
//    }
//    for (i in 1 until 10) {
//        val y = size.height * (i / 10f)
//        drawLine(
//            start = androidx.compose.ui.geometry.Offset(0f, y),
//            end = androidx.compose.ui.geometry.Offset(size.width, y),
//            color = lineColor,
//            strokeWidth = 1f
//        )
//    }
//}
