package com.example.barchart.ui.theme

data class History(
    val data: List<HistoricalData>? = emptyList()
)

data class HistoricalData(
    val year: String,
    val equity: internalValue,
    val debt: internalValue,
    val others: internalValue,
    val commodity: internalValue
)

data class internalValue(
    val amount: Double = 0.0,
    var height: Float = 0f,
    val percentage: String = ""
)