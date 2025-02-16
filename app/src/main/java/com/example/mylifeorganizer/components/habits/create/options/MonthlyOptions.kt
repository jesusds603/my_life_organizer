package com.example.mylifeorganizer.components.habits.create.options

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyOptions() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val isMonthlyAnytimeHabit = appViewModel.isMonthlyAnytimeHabit.value // Si es anytime en monthly
    val numDaysForMonthlyHabit = appViewModel.numDaysForMonthlyHabit.value // Si es anytime en monthly seleccionar el numero de veces (entre 1 y 31)
    val recurrenceMonthDaysHabit = appViewModel.recurrenceMonthDaysHabit.value

    Row {
        Button(
            onClick = { appViewModel.changeIsMonthlyAnytimeHabit(true) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isMonthlyAnytimeHabit) Color.Magenta else themeColors.backGround1
            )
        ) {
            Text(
                text = if (isLangEng) "Anytime" else "Cualquier día",
                color = themeColors.text1
            )
        }
        Button(
            onClick = { appViewModel.changeIsMonthlyAnytimeHabit(false) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isMonthlyAnytimeHabit) Color.Magenta else themeColors.backGround1
            )
        ) {
            Text(
                text = if(isLangEng) "Specific days" else "Días específicos",
                color = themeColors.text1
            )
        }
    }

    if (isMonthlyAnytimeHabit) {
        Text(
            text = if(isLangEng) "Select the amount of days" else "Selecciona la cantidad de días",
            color = themeColors.text1
        )
        LazyRow {
            items(31) { day ->
                Box(
                    modifier = Modifier
                        .clickable { appViewModel.changeNumDaysMonthlyHabit(day + 1) }
                        .background(if (numDaysForMonthlyHabit == day + 1) Color.Magenta else themeColors.backGround1)
                        .padding(8.dp)
                ) {
                    Text(
                        text = (day + 1).toString(),
                        color = themeColors.text1
                    )
                }
            }
        }
    } else {
        Text(
            text = if(isLangEng) "Select the days" else "Selecciona los días",
            color = themeColors.text1
        )
        LazyRow {
            items(31) { day ->
                Box(
                    modifier = Modifier
                        .clickable {
                            val selectedDays = recurrenceMonthDaysHabit.split(",").toMutableList()
                            if (selectedDays.contains((day + 1).toString())) {
                                selectedDays.remove((day + 1).toString())
                            } else {
                                selectedDays.add((day + 1).toString())
                            }
                            appViewModel.changeRecurrenceMonthDaysHabit(selectedDays.joinToString(","))
                        }
                        .background(if (recurrenceMonthDaysHabit.contains((day + 1).toString())) Color.Magenta else themeColors.backGround1)
                        .padding(8.dp)
                ) {
                    Text(
                        text = (day + 1).toString(),
                        color = themeColors.text1
                    )
                }
            }
        }
    }
}