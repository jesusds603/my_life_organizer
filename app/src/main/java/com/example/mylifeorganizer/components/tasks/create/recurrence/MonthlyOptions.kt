package com.example.mylifeorganizer.components.tasks.create.recurrence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyOptions (
    selectedMonthDaysNewTask: List<Int>,
    numMonthsNewTask: Int,
){
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val days = (1..31).map { it.toString() } + if(isLangEng) "Last day of month" else "Último día del mes"


    // LazyRow para seleccionar los días del mes
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(days) { day ->
            val isSelected = selectedMonthDaysNewTask.contains(
                if (day == "Último día del mes" || day == "Last day of month") -1 else day.toInt()
            )

            Text(
                text = day,
                color = if (isSelected) themeColors.tabButtonSelected else themeColors.text1,
                modifier = Modifier
                    .background(if (isSelected) themeColors.bgCardNote else themeColors.backGround1)
                    .clickable {
                        val value = if (day == "Último día del mes" || day == "Last day of month") -1 else day.toInt()
                        if (isSelected) {
                            appViewModel.updateSelectedMonthDaysNewTask(
                                selectedMonthDaysNewTask.toMutableList().apply {
                                    remove(value) }
                            )
                        } else {
                            appViewModel.updateSelectedMonthDaysNewTask(
                                selectedMonthDaysNewTask.toMutableList().apply {
                                    add(value) }
                            )
                        }
                    }
                    .padding(8.dp)
            )
        }
    }

    // Input para ingresar el número de meses
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if(isLangEng) "Num. of months" else "Núm. de meses",
            color = themeColors.text1
        )

        // Input para el número de meses
        TextField(
            value = numMonthsNewTask.toString(),
            onValueChange = {
                // Solo aceptar valores numéricos
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    appViewModel.updateNumMonthsNewTask(if (it.isEmpty()) 0 else it.toInt())
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.width(100.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text1,
                focusedContainerColor = themeColors.backGround1,
                unfocusedContainerColor = themeColors.backGround1
            )
        )
    }
}