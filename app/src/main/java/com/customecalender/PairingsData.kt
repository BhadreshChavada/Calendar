package com.customecalender

import java.util.Date

data class PairingsData(
    val EndDate: Date,
    val OriginalStartDate: Date,
    val TripBreakdown: List<TripBreakdownData>
)

data class TripBreakdownData(val BreakdownType: Int, val Percentage: Double)