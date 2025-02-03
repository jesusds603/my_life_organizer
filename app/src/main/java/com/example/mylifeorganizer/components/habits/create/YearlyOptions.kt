package com.example.mylifeorganizer.components.habits.create

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
fun YearlyOptions() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val isYearlyAnytimeHabit = appViewModel.isYearlyAnytimeHabit.value
    val numDaysForYearlyHabit = appViewModel.numDaysForYearlyHabit.value
    val recurrenceYearDaysHabit = appViewModel.recurrenceYearDaysHabit.value


    Row {
        Button(
            onClick = { appViewModel.changeIsYearlyAnytimeHabit(true) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isYearlyAnytimeHabit) Color.Magenta else themeColors.backGround1
            )
        ) {
            Text(
                text = "Anytime",
                color = themeColors.text1
            )
        }
        Button(
            onClick = { appViewModel.changeIsYearlyAnytimeHabit(false) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isYearlyAnytimeHabit) Color.Magenta else themeColors.backGround1
            )
        ) {
            Text(
                text = "Select Days",
                color = themeColors.text1
            )
        }
    }

    if (isYearlyAnytimeHabit) {
        Text(
            text = "Select the amount of days",
            color = themeColors.text1
        )
        LazyRow {
            items(365) { day ->
                Box(
                    modifier = Modifier
                        .clickable { appViewModel.changeNumDaysYearlyHabit(day + 1) }
                        .background(if (numDaysForYearlyHabit == day + 1) Color.Magenta else themeColors.backGround1)
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
            text = "Select the days you want to repeat",
            color = themeColors.text1
        )
        LazyRow {
            items(365) { day ->
                Box(
                    modifier = Modifier
                        .clickable {
                            val selectedDays = recurrenceYearDaysHabit.split(",").toMutableList()
                            if (selectedDays.contains((day + 1).toString())) {
                                selectedDays.remove((day + 1).toString())
                            } else {
                                selectedDays.add((day + 1).toString())
                            }
                            appViewModel.changeRecurrenceYearDaysHabit(selectedDays.joinToString(","))
                        }
                        .background(if (recurrenceYearDaysHabit.contains((day + 1).toString())) Color.Magenta else themeColors.backGround1)
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