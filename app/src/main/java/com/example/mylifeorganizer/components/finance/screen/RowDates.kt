package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowDates(
    financeWithCategories: List<FinanceWithCategories>
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value


    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val monthFormatter = DateTimeFormatter.ofPattern("yyyy MMM", Locale.getDefault())
    val selectedMonthFormatter = DateTimeFormatter.ofPattern("yyyy/MM")

    // Obtener la fecha actual
    val currentDate = LocalDate.now()

    // Encontrar la fecha mínima y máxima si la lista no está vacía
    val minDate = financeWithCategories.minByOrNull { it.finance.date }?.finance?.date?.let {
        LocalDate.parse(it, dateFormatter)
    } ?: currentDate

    val maxDate = financeWithCategories.maxByOrNull { it.finance.date }?.finance?.date?.let {
        LocalDate.parse(it, dateFormatter)
    } ?: currentDate

    // Generar la lista de meses entre minDate y maxDate
    val monthsList = mutableListOf<LocalDate>()
    var tempDate = YearMonth.from(minDate).atDay(1)
    val endDate = YearMonth.from(maxDate).atEndOfMonth()

    while (!tempDate.isAfter(endDate)) {
        monthsList.add(tempDate)
        tempDate = tempDate.plusMonths(1)
    }

    val selectedMonthScreen = appViewModel.selectedMonthScreen


    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(monthsList) { date ->
            val monthString = date.format(selectedMonthFormatter)
            val isSelected = monthString == selectedMonthScreen

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(themeColors.backGround1)
                    .clickable {
                        appViewModel.updateSelectedMonthScreen(monthString)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = date.format(monthFormatter),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
                )
            }
        }
    }


}