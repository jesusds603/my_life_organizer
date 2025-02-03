package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.HabitEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainDialog() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val titleNewHabit = appViewModel.titleNewHabit.value
    val colorNewHabit = appViewModel.colorNewHabit.value
    val durationForNewHabit = appViewModel.durationForNewHabit.value
    val recurrencePatternForNewHabit = appViewModel.recurrencePatternForNewHabit.value // "daily", "weekly", "monthly", "yearly"
    val numDaysForWeeklyHabit = appViewModel.numDaysForWeeklyHabit.value // Si es anytime en weekly seleccionar el numero de veces entre 1 y 7
    val recurrenceWeekDaysHabit = appViewModel.recurrenceWeekDaysHabit.value // Si no es anytime entonces una lista de los indices de los dias de 0 a 6
    val numDaysForMonthlyHabit = appViewModel.numDaysForMonthlyHabit.value // Si es anytime en monthly seleccionar el numero de veces (entre 1 y 31)
    val recurrenceMonthDaysHabit = appViewModel.recurrenceMonthDaysHabit.value
    val numDaysForYearlyHabit = appViewModel.numDaysForYearlyHabit.value
    val recurrenceYearDaysHabit = appViewModel.recurrenceYearDaysHabit.value
    val timeForNewHabit = appViewModel.timeForNewHabit.value


    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleAddingHabit()
        },
        confirmButton = {
            Button(
                onClick = {
                    noteViewModel.addHabit(
                        HabitEntity(
                            title = titleNewHabit,
                            color = colorNewHabit,
                            doItAt = timeForNewHabit,

                            recurrencePattern = recurrencePatternForNewHabit,
                            recurrenceWeekDays = recurrenceWeekDaysHabit,
                            numDaysForWeekly = numDaysForWeeklyHabit,
                            recurrenceMonthDays = recurrenceMonthDaysHabit,
                            numDaysForMonthly = numDaysForMonthlyHabit,
                            recurrenceYearDays = recurrenceYearDaysHabit,
                            numDaysForYearly = numDaysForYearlyHabit,

                            duration = durationForNewHabit
                        )
                    )

                    appViewModel.toggleAddingHabit()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround3
                )
            ) {
                Text(
                    text = "Aceptar",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingHabit()

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround3
                )
            ) {
                Text(
                    text = "Cancelar",
                    color = themeColors.text1
                )
            }
        },
        title = {
            Text(
                text = "Create Habit",
                color = themeColors.text1
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SelectTitle()

                HorizontalDivider(thickness = 1.dp, color = themeColors.text3)
                Spacer(modifier = Modifier.height(16.dp))

                SelectRecurrence()

                HorizontalDivider(thickness = 1.dp, color = themeColors.text3)
                Spacer(modifier = Modifier.height(16.dp))

                SelectTime()

                HorizontalDivider(thickness = 1.dp, color = themeColors.text3)
                Spacer(modifier = Modifier.height(16.dp))

                SelectDuration()

                HorizontalDivider(thickness = 1.dp, color = themeColors.text3)
                Spacer(modifier = Modifier.height(16.dp))

                SelectColors()
            }

        },
        containerColor = themeColors.backGround2
    )
}