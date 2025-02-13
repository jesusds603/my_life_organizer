package com.example.mylifeorganizer.components.dashboard

import android.util.Log
import androidx.compose.ui.graphics.toArgb
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter

// Helper Function to Prepare Pie Chart Data
fun preparePieChartData(totalIncome: Double, totalExpense: Double, themeViewModel: ThemeViewModel): PieData {
    // Calculate the total sum of incomes and expenses
    val totalSum = totalIncome + totalExpense

    Log.d("", "$totalIncome, $totalExpense, $totalSum")

    // Handle zero values
    val incomePercentage = if (totalSum == 0.0) 50f else (totalIncome / totalSum * 100).toFloat()
    val expensePercentage = if (totalSum == 0.0) 50f else (totalExpense / totalSum * 100).toFloat()

    // Create entries
    val entries = listOf(
        PieEntry(incomePercentage, "Income: $${"%.2f".format(totalIncome)}"),
        PieEntry(expensePercentage, "Expense: $${"%.2f".format(totalExpense)}")
    )

    // Create dataset
    val dataSet = PieDataSet(entries, "").apply {
        colors = listOf(themeViewModel.themeColors.value.bgIncome.toArgb(), themeViewModel.themeColors.value.bgExpense.toArgb()) // Set slice colors
        valueTextColor = themeViewModel.themeColors.value.text1.toArgb() // Set value text color
        valueTextSize = 14f // Set value text size
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                // Display percentages
                return if (value == 50f && totalSum == 0.0) "0%" else "%.1f%%".format(value)
            }
        }
    }

    return PieData(dataSet)
}