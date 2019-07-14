package com.mfkw.compass.screen.main.view

import kotlin.math.absoluteValue

private const val COORDINATE_DECIMAL_DIGITS = 4

class MainScreenViewFormatHelper {
    fun formatLatitude(latitude: Double) =
        latitude.formatWithSignReplacement("N", "S")
    fun formatLongitude(longitude: Double) =
        longitude.formatWithSignReplacement("E","W")

    private fun Number.toFixed(decimalDigits: Int) = "%.${decimalDigits}f".format(this)
    private fun <T> Double.switchOnSign(ifPositive: T, ifNegative: T, otherwise: T) =
        if(this > 0) ifPositive else if(this < 0) ifNegative else otherwise
    private fun Double.formatWithSignReplacement(ifPositive: String, ifNegative: String) =
        absoluteValue.toFixed(COORDINATE_DECIMAL_DIGITS) + " " + switchOnSign(ifPositive, ifNegative, "")
}
