package com.zalo.proyectmeli.utils

import java.text.NumberFormat
import java.util.Locale

object FormatNumber {
    fun formatNumber(number: Double): String {
        val value: Double = number
        val region = Locale.getDefault()
        val formatMoney = NumberFormat.getCurrencyInstance(region)
        val convert = formatMoney.format(value)
        val notDecimal = convert.substring(0, convert.length - 3)
        return (notDecimal)
    }
}
