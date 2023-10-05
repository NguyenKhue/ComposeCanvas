package com.khue.composecanvas.chart.market_chart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Candle(
    val time: LocalDateTime,
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float
) : Comparable<Candle>, Parcelable {

    override fun compareTo(other: Candle) = if (time.isBefore(other.time)) -1 else 1
}
