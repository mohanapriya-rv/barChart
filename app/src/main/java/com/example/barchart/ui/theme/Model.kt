package com.example.barchart.ui.theme

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.barchart.R


data class HistoryData(
    val data: List<HistoricalData1>? = emptyList()
)

data class HistoricalData1(
    val year: String,
    val equity: internalValue1,
    val debt: internalValue1,
    val others: internalValue1,
    val commodity: internalValue1
)

data class internalValue1(
    val amount: Double = 0.0,
    var height: Float = 0f,
    val percentage: String = ""
)

data class FIBarChartDataSet(
    val name: String,
    val value: Double,
    val color: Color,
    val percentage: String = "",
    var height: Float = 0f
)

data class FISegmentBarChartModel(
    val title: String,
    val segments: List<FIBarChartDataSet>
)

fun processData(
    historicalAssetValue: List<HistoricalAssetAllocation>, context: Context
): List<FISegmentBarChartModel> {
    val fiCustomChartList: MutableList<FISegmentBarChartModel> = mutableListOf()
    historicalAssetValue.forEach {
        val fiChartData = convertToFISegmentBarChartModel(it, context)
        fiCustomChartList.add(fiChartData)

    }
    return fiCustomChartList.toList()

}


fun convertToFISegmentBarChartModel(
    historicalData: HistoricalAssetAllocation, context: Context
): FISegmentBarChartModel {
    val segments = mutableListOf<FIBarChartDataSet>()


    historicalData.others?.let {
        segments.add(
            FIBarChartDataSet(
                "Others", it, Color.Yellow
            )
        )
    }
    historicalData.commodity?.let {
        segments.add(
            FIBarChartDataSet(
                "Commodity", it, Color.Red
            )
        )
    }
    historicalData.hybrid?.let {
        segments.add(
            FIBarChartDataSet(
                "Hybrid",
                historicalData.hybrid,
                Color.LightGray
            )
        )
    }
    historicalData.debt?.let {
        segments.add(
            FIBarChartDataSet(
                "Debt", historicalData.debt, Color.Green
            )
        )
    }
    historicalData.gold?.let {
        segments.add(
            FIBarChartDataSet(
                "Gold", historicalData.gold, Color.Gray
            )
        )
    }


    historicalData.equity?.let {
        segments.add(
            FIBarChartDataSet("Equity", historicalData.equity, Color.White)

        )
    }


    val title = historicalData.year ?: "Unknown Year"

    return FISegmentBarChartModel(title, segments)
}


data class HistoricalAssetAllocation(
    val year: String? = null,
    val equity: Double? = null,
    val debt: Double? = null,
    val gold: Double? = null,
    val hybrid: Double? = null,
    val commodity: Double? = null,
    val others: Double? = null,
    val index: Int? = 0,
)


fun getHistoricalAssetAllocations(): List<HistoricalAssetAllocation> {
    return listOf(
        HistoricalAssetAllocation(
            year = "2020",
            equity = 25000.0,
            debt = 25000.0,
            gold = 2000.0,
            hybrid = 5000.0,
            commodity = 3000.0,
            others = 500.0,
            index = 1
        ),
        HistoricalAssetAllocation(
            year = "2021",
            equity = 30000.0,
            debt = 20000.0,
            gold = 2500.0,
            hybrid = 6000.0,
            commodity = 3500.0,
            others = 600.0,
            index = 2
        ),
    )
}
