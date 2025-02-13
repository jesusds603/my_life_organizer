package com.example.mylifeorganizer.components.dashboard

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.room.PaymentMethodEntity
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


fun prepareCatChartData(
    finances: List<FinanceWithCategories>,
    categories: List<CategoryFinanceEntity>,
    themeViewModel: ThemeViewModel
): BarData {
    val incomeEntries = mutableListOf<BarEntry>()
    val expenseEntries = mutableListOf<BarEntry>()
    val colorS = mutableListOf<Int>()

    categories.forEachIndexed { index, category ->
        val incomeTotal = finances.filter { it.finance.type == "income" && it.categories.contains(category) }
            .sumOf { it.finance.amount }
        val expenseTotal = finances.filter { it.finance.type == "expense" && it.categories.contains(category) }
            .sumOf { it.finance.amount }

        if (incomeTotal > 0 || expenseTotal > 0) {
            incomeEntries.add(BarEntry(index.toFloat(), incomeTotal.toFloat()))
            expenseEntries.add(BarEntry(index.toFloat() + 0.4f, expenseTotal.toFloat())) // Mueve la barra a la derecha

            colorS.add(themeViewModel.getCategoryColor(category.bgColor).toArgb()) // Color de la categoría
        }
    }

    val incomeDataSet = BarDataSet(incomeEntries, "Income").apply {
        valueTextColor = Color.Green.toArgb()
        valueTextSize = 12f
        colors = colorS
    }

    val expenseDataSet = BarDataSet(expenseEntries, "Expense").apply {
        valueTextColor = Color.Red.toArgb()
        valueTextSize = 12f
        colors = colorS
    }

    return BarData(incomeDataSet, expenseDataSet).apply {
        barWidth = 0.3f // Tamaño de cada barra
    }
}

fun preparePaymentChartData(
    finances: List<FinanceWithCategories>,
    paymentMethods: List<PaymentMethodEntity>,
    themeViewModel: ThemeViewModel
): BarData {
    val incomeEntries = mutableListOf<BarEntry>()
    val expenseEntries = mutableListOf<BarEntry>()
    val colorsPay = mutableListOf<Int>()

    paymentMethods.forEachIndexed{ index, paymentMethod ->
        val incomeTotal = finances.filter { it.finance.type == "income" && it.finance.paymentId == paymentMethod.paymentId }
            .sumOf { it.finance.amount }
        val expenseTotal = finances.filter { it.finance.type == "expense" && it.finance.paymentId == paymentMethod.paymentId }
            .sumOf { it.finance.amount }

        if (incomeTotal > 0 || expenseTotal > 0) {
            incomeEntries.add(BarEntry(index.toFloat(), incomeTotal.toFloat()))
            expenseEntries.add(BarEntry(index.toFloat() + 0.4f, expenseTotal.toFloat())) // Mueve la barra a la derecha
        }
        colorsPay.add(themeViewModel.getCategoryColor(paymentMethod.bgColor).toArgb())

    }

    val incomeDataSet = BarDataSet(incomeEntries, "Income").apply {
        valueTextColor = Color.Green.toArgb()
        valueTextSize = 12f
        colors = colorsPay
    }

    val expenseDataSet = BarDataSet(expenseEntries, "Expense").apply {
        valueTextColor = Color.Red.toArgb()
        valueTextSize = 12f
        colors = colorsPay
    }

    return BarData(incomeDataSet, expenseDataSet).apply {
        barWidth = 0.3f // Tamaño de cada barra
    }
}