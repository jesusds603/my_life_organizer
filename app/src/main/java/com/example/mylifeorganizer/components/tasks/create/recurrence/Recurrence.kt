package com.example.mylifeorganizer.components.tasks.create.recurrence

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Recurrence() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val isTaskRecurring = appViewModel.isTaskRecurring
    val recurrenceTaskPattern = appViewModel.recurrenceTaskPattern
    val numDaysNewTask = appViewModel.numDaysNewTask
    val selectedWeekDaysNewTask = appViewModel.selectedWeekDaysNewTask
    val numWeeksNewTask = appViewModel.numWeeksNewTask
    val selectedMonthDaysNewTask = appViewModel.selectedMonthDaysNewTask
    val numMonthsNewTask = appViewModel.numMonthsNewTask
    val selectedYearDaysNewTask = appViewModel.selectedYearDaysNewTask
    val numYearsNewTask = appViewModel.numYearsNewTask
    val selectedCustomIntervalNewTask = appViewModel.selectedCustomIntervalNewTask
    val numTimesNewTask = appViewModel.numTimesNewTask

    val recurrences = mapOf(
        "daily" to Pair("Daily", "Diario"),
        "weekly" to Pair("Weekly", "Semanal"),
        "monthly" to Pair("Monthly", "Mensual"),
        "yearly" to Pair("Yearly", "Anual"),
        "custom" to Pair("Custom", "Personalizado")
    )

    fun translateRecurrence(recurrence: String, isLangEng: Boolean): String {
        return if (isLangEng) {
            recurrences[recurrence]?.first ?: recurrence // Si no existe, devuelve la clave
        } else {
            recurrences[recurrence]?.second ?: recurrence // Si no existe, devuelve la clave
        }
    }


    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            Text(
                text = if(isLangEng) "Recurring:  " else "Recurrente:  ",
                color = themeColors.text1
            )

            Text(
                text = if (isTaskRecurring) "ON" else "OFF",
                color = themeColors.text1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        appViewModel.toggleIsTaskRecurring()
                        Log.d("isTaskRecurring", isTaskRecurring.toString())
                    }
            )
        }

        if(isTaskRecurring) {
            Text(
                text = "${if(isLangEng) "Recurrence pattern" else "Patrón de recurrencia"}: " + translateRecurrence(recurrenceTaskPattern, isLangEng),
                color = themeColors.text1,
            )

            LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(listOf("daily", "weekly", "monthly", "yearly", "custom")) { recurrence ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(color = themeColors.backGround1)
                            .clickable {
                                appViewModel.updateRecurrenceTaskPattern(recurrence)
                            }
                    ) {
                        // Obtener la traducción según el valor de isLangEng
                        val text = if (isLangEng) {
                            recurrences[recurrence]?.first ?: recurrence // Si no existe, mostrar la clave
                        } else {
                            recurrences[recurrence]?.second ?: recurrence // Si no existe, mostrar la clave
                        }

                        Text(
                            text = text,
                            color = if(recurrence == recurrenceTaskPattern) {
                                themeColors.tabButtonSelected
                            } else {
                                themeColors.tabButtonDefault
                            }
                        )
                    }

                }
            }

            when (recurrenceTaskPattern) {
                "daily" -> {
                    DailyOptions(
                        numDaysNewTask
                    )
                }

                "weekly" -> {
                    WeeklyOptions(
                        selectedWeekDaysNewTask,
                        numWeeksNewTask
                    )
                }

                "monthly" -> {
                    MonthlyOptions(
                        selectedMonthDaysNewTask,
                        numMonthsNewTask
                    )
                }

                "yearly" -> {
                    YearlyOptions(
                        selectedYearDaysNewTask,
                        numYearsNewTask
                    )
                }

                "custom" -> {
                    CustomOptions(
                        selectedCustomIntervalNewTask,
                        numTimesNewTask
                    )
                }
            }
        }
    }
}