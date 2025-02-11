package com.example.mylifeorganizer.screens

import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var selectedYear by remember { mutableStateOf(YearMonth.now().year) }
    var selectedMonth by remember { mutableStateOf(YearMonth.now().monthValue) }

    val monthString = selectedMonth.toString().padStart(2, '0')
    val financesForMonth by noteViewModel.getFinancesByMonth("$selectedYear/$monthString").collectAsState(initial = emptyList())
    val categoriesFinance by noteViewModel.categoriesFinance.collectAsState(initial = emptyList())
    val paymentMethods by noteViewModel.paymentMethods.collectAsState(initial = emptyList())

    val totalExpense = financesForMonth.filter { it.finance.type == "expense" }.sumOf { it.finance.amount }
    val totalIncome = financesForMonth.filter { it.finance.type == "income" }.sumOf { it.finance.amount }
    val balance = totalIncome - totalExpense

    // Prepare data for charts
    val pieChartData by remember(totalIncome, totalExpense) {
        mutableStateOf(preparePieChartData(totalIncome, totalExpense, themeViewModel))
    }
    val barChartData = prepareBarChartData(financesForMonth, categoriesFinance, themeViewModel)
    val lineChartData = prepareLineChartData(financesForMonth, categoriesFinance, themeViewModel)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Selector de Año y Mes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Año con flechas
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                    contentDescription = "Año anterior",
                    tint = themeColors.text1,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { selectedYear-- }
                )
                Text(
                    text = selectedYear.toString(),
                    color = themeColors.text1
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                    contentDescription = "Año siguiente",
                    tint = themeColors.text1,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { selectedYear++ }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Mes con flechas
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                    contentDescription = "Mes anterior",
                    tint = themeColors.text1,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            if (selectedMonth == 1) {
                                selectedMonth = 12
                                selectedYear--
                            } else {
                                selectedMonth--
                            }
                        }
                )
                Text(
                    text = monthString,
                    color = themeColors.text1
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                    contentDescription = "Mes siguiente",
                    tint = themeColors.text1,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            if (selectedMonth == 12) {
                                selectedMonth = 1
                                selectedYear++
                            } else {
                                selectedMonth++
                            }
                        }
                )
            }
        }

        Text(
            text = "$totalIncome, $totalExpense, $balance, ",
            color = themeColors.text1
        )

        Text(
            text = if(totalIncome + totalExpense == 0.0) {
                "Suma es cero"
            } else {
                "Suma no cero"
            },
            color = themeColors.text1
        )


        // Pie Chart for Income vs Expense
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    // Configure the chart
                    description.isEnabled = false // Disable description
                    setEntryLabelColor(themeColors.text1.toArgb()) // Set label color
                    legend.isEnabled = true // Enable legend
                    legend.textColor = themeColors.text1.toArgb() // Set legend text color
                    setUsePercentValues(false) // Show actual values instead of percentages
                    setDrawEntryLabels(true) // Show labels on slices
                    animateY(1000) // Add animation
                    setHoleColor(Color.Transparent.toArgb())

                    // Set data
                    data = pieChartData
                }
            },
            update = { chart ->
                chart.data = pieChartData
                chart.invalidate() // Redibujar la gráfica cuando los datos cambien
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        // Bar Chart for Category-wise Expenses
        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    data = barChartData
                    description.isEnabled = false
                    xAxis.valueFormatter = IndexAxisValueFormatter(categoriesFinance.map { it.name })
                    animateY(1000)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        // Line Chart for Category-wise Trends
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    data = lineChartData
                    description.isEnabled = false
                    xAxis.valueFormatter = IndexAxisValueFormatter(categoriesFinance.map { it.name })
                    animateY(1000)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

// Helper Function to Prepare Pie Chart Data
private fun preparePieChartData(totalIncome: Double, totalExpense: Double, themeViewModel: ThemeViewModel): PieData {
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

private fun prepareBarChartData(
    finances: List<FinanceWithCategories>,
    categories: List<CategoryFinanceEntity>,
    themeViewModel: ThemeViewModel
): BarData {
    val categoryAmounts = categories.associate { it.name to 0f }.toMutableMap()
    finances.filter { it.finance.type == "expense" }.forEach { finance ->
        finance.categories.forEach { category ->
            categoryAmounts[category.name] = categoryAmounts[category.name]!! + finance.finance.amount.toFloat()
        }
    }
    val entries = categories.mapIndexed { index, category ->
        BarEntry(index.toFloat(), categoryAmounts[category.name]!!)
    }
    val dataSet = BarDataSet(entries, "Expenses by Category").apply {
        colors = categories.map { themeViewModel.getCategoryColor(it.bgColor).toArgb() }
        valueTextColor = Color.Black.toArgb()
        valueTextSize = 12f
    }
    return BarData(dataSet)
}

private fun prepareLineChartData(
    finances: List<FinanceWithCategories>,
    categories: List<CategoryFinanceEntity>,
    themeViewModel: ThemeViewModel
): LineData {
    val categoryAmounts = categories.associate { it.name to 0f }.toMutableMap()
    finances.filter { it.finance.type == "expense" }.forEach { finance ->
        finance.categories.forEach { category ->
            categoryAmounts[category.name] = categoryAmounts[category.name]!! + finance.finance.amount.toFloat()
        }
    }
    val entries = categories.mapIndexed { index, category ->
        Entry(index.toFloat(), categoryAmounts[category.name]!!)
    }
    val dataSet = LineDataSet(entries, "Expenses Trend by Category").apply {
        colors = categories.map { themeViewModel.getCategoryColor(it.bgColor).toArgb() }
        valueTextColor = Color.Black.toArgb()
        valueTextSize = 12f
    }
    return LineData(dataSet)
}