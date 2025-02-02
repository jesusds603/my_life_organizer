package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectRecurrence() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val recurrencePatternForNewHabit = appViewModel.recurrencePatternForNewHabit.value // "daily", "weekly", "monthly", "yearly"
    val numDaysForWeeklyHabit = appViewModel.numDaysForWeeklyHabit.value // Si es anytime en weekly seleccionar el numero de veces entre 1 y 7
    val recurrenceWeekDaysHabit = appViewModel.recurrenceWeekDaysHabit.value // Si no es anytime entonces una lista de los indices de los dias de 0 a 6
    val numDaysForMonthlyHabit = appViewModel.numDaysForMonthlyHabit.value // Si es anytime en monthly seleccionar el numero de veces (entre 1 y 31)
    val recurrenceMonthDaysHabit = appViewModel.recurrenceMonthDaysHabit.value
    val numDaysForYearlyHabit = appViewModel.numDaysForYearlyHabit.value
    val recurrenceYearDaysHabit = appViewModel.recurrenceYearDaysHabit.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // LazyRow para los botones de recurrencia
        LazyRow {
            items(listOf("Daily", "Weekly", "Monthly", "Yearly")) { recurrenceType ->
                val isSelected = recurrencePatternForNewHabit == recurrenceType.lowercase()
                Button(
                    onClick = { appViewModel.changeRecurrencePatternForNewHabit(recurrenceType.lowercase()) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color.Magenta else themeColors.backGround1

                    )
                ) {
                    Text(
                        text = recurrenceType,
                        color = themeColors.text1
                    )
                }
            }
        }

        // Dependiendo de la selección, mostramos las opciones adicionales
        when (recurrencePatternForNewHabit) {
            "weekly" -> {
                WeeklyOptions(
                    appViewModel = appViewModel,
                    numDaysForWeeklyHabit = numDaysForWeeklyHabit,
                    recurrenceWeekDaysHabit = recurrenceWeekDaysHabit
                )
            }
            "monthly" -> {
                MonthlyOptions(
                    appViewModel = appViewModel,
                    numDaysForMonthlyHabit = numDaysForMonthlyHabit,
                    recurrenceMonthDaysHabit = recurrenceMonthDaysHabit
                )
            }
            "yearly" -> {
                YearlyOptions(
                    appViewModel = appViewModel,
                    numDaysForYearlyHabit = numDaysForYearlyHabit,
                    recurrenceYearDaysHabit = recurrenceYearDaysHabit
                )
            }
        }
    }
}

