package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.room.PaymentMethodEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    financeWithCategories: List<FinanceWithCategories>
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val paymentMethods = noteViewModel.paymentMethods.collectAsState(initial = emptyList()).value

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val selectedMonthFormatter = DateTimeFormatter.ofPattern("yyyy/MM")

    val selectedCategoryForFinanceScreen = appViewModel.selectedCategoryForFinanceScreen
    val selectedPaymentForFinanceScreen = appViewModel.selectedPaymentMethodForFinanceScreen
    val selectedMonthScreen = appViewModel.selectedMonthScreen

    // 🔹 Filtrar finanzas según el mes seleccionado
    var filteredFinances = financeWithCategories.filter { finance ->
        val financeMonth = LocalDate.parse(finance.finance.date, dateFormatter).format(selectedMonthFormatter)
        financeMonth == selectedMonthScreen
    }

    // 🔹 Filtrar finanzas según la categoría seleccionada (si no es "All")
    if (selectedCategoryForFinanceScreen != "All") {
        filteredFinances = filteredFinances.filter { finance ->
            // Filtramos las finanzas por las categorías seleccionadas
            finance.categories.any { it.name == selectedCategoryForFinanceScreen }
        }
    }

    // 🔹 Filtrar finanzas según el método de pago seleccionado (si no es "All")
    if (selectedPaymentForFinanceScreen != "All") {
        // Buscamos el paymentId correspondiente al nombre del método de pago seleccionado
        val selectedPaymentMethod = paymentMethods.find { it.name == selectedPaymentForFinanceScreen }
        selectedPaymentMethod?.let { paymentMethod ->
            // Filtramos las finanzas que tengan el paymentId igual al del método de pago seleccionado
            filteredFinances = filteredFinances.filter { finance ->
                finance.finance.paymentId == paymentMethod.paymentId
            }
        }
    }

    // 🔹 Agrupar las finanzas por fecha
    val groupedFinances = filteredFinances
        .groupBy { LocalDate.parse(it.finance.date, dateFormatter).toString() }
        .toSortedMap(reverseOrder()) // Esto ordena las fechas de forma ascendente



    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 🔹 Aquí podrías mostrar las finanzas filtradas
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(groupedFinances.toList()) { (date, financesForDay) ->
                val totalExpense = financesForDay.filter { it.finance.type == "expense" }.sumOf { it.finance.amount }
                val totalIncome = financesForDay.filter { it.finance.type == "income" }.sumOf { it.finance.amount }
                val balance = totalIncome - totalExpense

                Row {
                    // Mostrar la fecha
                    Text(
                        text = date, // Este es el día en formato yyyy/MM/dd
                        color = themeColors.text1,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 1.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    )

                    Text(
                        text = "$ $totalIncome",
                        color = themeColors.text1,
                        modifier = Modifier
                            .background(color = themeColors.bgIncome)
                            .padding(2.dp)
                    )

                    Text(
                        text = "$ $totalExpense",
                        color = themeColors.text1,
                        modifier = Modifier
                            .background(color = themeColors.bgExpense)
                            .padding(2.dp)
                    )

                    Text(
                        text = "$ $balance",
                        color = themeColors.text1,
                        modifier = Modifier
                            .background(color = themeColors.backGround1)
                            .padding(2.dp)
                    )
                }

                // Mostrar las finanzas de ese día
                financesForDay.forEach { financeItem ->
                    val paymentMethod = paymentMethods.find { it.paymentId == financeItem.finance.paymentId }

                    FinanceCard(
                        finance = financeItem,
                        paymentMethod = paymentMethod ?: PaymentMethodEntity(0, "", "")
                    )
                }
            }
        }
    }
}
