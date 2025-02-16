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
fun WeeklyOptions() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val isWeeklyAnytimeHabit = appViewModel.isWeeklyAnytimeHabit.value // Si es anytime en weekly
    val numDaysForWeeklyHabit = appViewModel.numDaysForWeeklyHabit.value // Si es anytime en weekly seleccionar el numero de veces entre 1 y 7
    val recurrenceWeekDaysHabit = appViewModel.recurrenceWeekDaysHabit.value // Si no es anytime entonces una lista de los indices de los dias de 0 a 6


    Row {
        Button(
            onClick = { appViewModel.changeIsWeeklyAnytimeHabit(true) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isWeeklyAnytimeHabit) Color.Magenta else themeColors.backGround1
            )
        ) {
            Text(
                text = if(isLangEng) "Anytime" else "Cualquier día",
                color = themeColors.text1
            )
        }
        Button(
            onClick = { appViewModel.changeIsWeeklyAnytimeHabit(false) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isWeeklyAnytimeHabit) Color.Magenta else themeColors.backGround1
            )
        ) {
            Text(
                text = if(isLangEng) "Specific days" else "Días específicos",
                color = themeColors.text1

            )
        }
    }

    if (isWeeklyAnytimeHabit) {
        Text(
            text = if(isLangEng) "Select the amount of days" else "Selecciona la cantidad de días",
            color = themeColors.text1
        )
        LazyRow {
            items(7) { day ->
                Box(
                    modifier = Modifier
                        .clickable { appViewModel.changeNumDaysWeeklyHabit(day + 1) }
                        .background(if (numDaysForWeeklyHabit == day + 1) Color.Magenta else themeColors.backGround1)
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
        val daysOfWeek = if (isLangEng) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        } else {
            listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
        }
        Text(
            text = if(isLangEng) "Select the days" else "Selecciona los días",
            color = themeColors.text1
        )
        LazyRow {
            items(daysOfWeek.size) { index ->
                Box(
                    modifier = Modifier
                        .clickable {
                            val selectedDays = recurrenceWeekDaysHabit.split(",").toMutableList()
                            if (selectedDays.contains(index.toString())) {
                                selectedDays.remove(index.toString())
                            } else {
                                selectedDays.add(index.toString())
                            }
                            appViewModel.changeRecurrenceWeekDaysHabit(selectedDays.joinToString(","))
                        }
                        .background(if (recurrenceWeekDaysHabit.contains(index.toString())) Color.Magenta else themeColors.backGround1)
                        .padding(8.dp)
                ) {
                    Text(
                        text = daysOfWeek[index],
                        color = themeColors.text1
                    )
                }
            }
        }
    }
}
