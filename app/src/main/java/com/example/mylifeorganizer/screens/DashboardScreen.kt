package com.example.mylifeorganizer.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.dashboard.RowSelector
import com.example.mylifeorganizer.components.dashboard.prepareCatChartData
import com.example.mylifeorganizer.components.dashboard.preparePaymentChartData
import com.example.mylifeorganizer.components.dashboard.preparePieChartData
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    var selectedYear by remember { mutableStateOf(YearMonth.now().year) }
    var selectedMonth by remember { mutableStateOf(YearMonth.now().monthValue) }

    val monthString = selectedMonth.toString().padStart(2, '0')
    val financesForMonth by noteViewModel.getFinancesByMonth("$selectedYear/$monthString").collectAsState(initial = emptyList())
    val categoriesFinance by noteViewModel.categoriesFinance.collectAsState(initial = emptyList())
    val paymentMethods by noteViewModel.paymentMethods.collectAsState(initial = emptyList())

    val totalExpense = financesForMonth.filter { it.finance.type == "expense" }.sumOf { it.finance.amount }
    val totalIncome = financesForMonth.filter { it.finance.type == "income" }.sumOf { it.finance.amount }
    val balance = totalIncome - totalExpense

    val scrollState by remember { mutableStateOf(0) }

    // Prepare data for charts
    val pieChartData by remember(totalIncome, totalExpense) {
        mutableStateOf(preparePieChartData(totalIncome, totalExpense, themeViewModel, isLangEng))
    }
    val catChartData by remember (financesForMonth, categoriesFinance) {
        mutableStateOf(
            prepareCatChartData(financesForMonth, categoriesFinance, themeViewModel, isLangEng)
        )
    }
    val paymentChartData by remember (financesForMonth, paymentMethods) {
        mutableStateOf(
            preparePaymentChartData(financesForMonth, paymentMethods, themeViewModel, isLangEng)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState(scrollState)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       RowSelector(
           selectedYear = selectedYear,
           onYearChange = { selectedYear = it },
           selectedMonth = selectedMonth,
           onMonthChange = { selectedMonth = it },
           monthString = monthString
       )

        Text(
            text = "Balance: $ $balance",
            color = themeColors.text1,
            fontWeight = FontWeight.Bold
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
                    isDrawHoleEnabled = false

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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if(isLangEng) "Category-wise Expenses and Income" else "Gastos y ganancias por categoría",
            color = themeColors.text1,
            fontWeight = FontWeight.Bold
        )

        // Bar Chart for Category-wise Expenses
        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    data = catChartData
                    description.isEnabled = false
                    setFitBars(true)
                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(categoriesFinance.map { it.name })
                        Log.d("DashboardScreen", "Categories: ${categoriesFinance.map { it.name }}")
                        position = XAxis.XAxisPosition.BOTTOM
                        granularity = 1f
                        textSize = 12f
                        labelRotationAngle = -90f // Texto vertical
                        textColor = themeColors.text1.toArgb()
                    }
                    axisLeft.axisMinimum = 0f
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    groupBars(0f, 0.2f, 0.05f) // Agrupa las barras
                    animateY(1000)
                }
            },
            update = {chart ->
                chart.data = catChartData
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(categoriesFinance.map { it.name })
                Log.d("DashboardScreen", "Updated Categories: ${categoriesFinance.map { it.name }}")
                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if(isLangEng) "Payment Method-wise Expenses and Income" else "Gastos y ganancias por método de pago",
            color = themeColors.text1,
            fontWeight = FontWeight.Bold
        )

        // Bar Chart for Payment Method-wise Expenses
        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    data = paymentChartData
                    description.isEnabled = false
                    setFitBars(true)
                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(paymentMethods.map { it.name })
                        position = XAxis.XAxisPosition.BOTTOM
                        granularity = 1f
                        textSize = 12f
                        labelRotationAngle = -90f // Texto vertical
                        textColor = themeColors.text1.toArgb()
                    }
                    axisLeft.axisMinimum = 0f
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    groupBars(0f, 0.2f, 0.05f) // Agrupa las barras
                    animateY(1000)
                }
            },
            update = {chart ->
                chart.data = paymentChartData
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(paymentMethods.map { it.name })
                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

    }
}


