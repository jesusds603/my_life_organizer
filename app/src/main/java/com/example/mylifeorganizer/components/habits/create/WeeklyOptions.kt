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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyOptions(
    appViewModel: AppViewModel,
    numDaysForWeeklyHabit: Int?,
    recurrenceWeekDaysHabit: String?
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var showAnytimeOptions by remember { mutableStateOf(numDaysForWeeklyHabit != null) }

    Row {
        Button(
            onClick = { showAnytimeOptions = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (showAnytimeOptions) Color.Magenta else themeColors.backGround1
            )
        ) {
            Text(
                text = "Anytime",
                color = themeColors.text1
            )
        }
        Button(
            onClick = { showAnytimeOptions = false },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!showAnytimeOptions) Color.Magenta else themeColors.backGround1
            )
        ) {
            Text(
                text ="Select Days",
                color = themeColors.text1

            )
        }
    }

    if (showAnytimeOptions) {
        Text(
            text = "Select the amount of days",
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
        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        Text(
            text = "Select the days you want to repeat",
            color = themeColors.text1
        )
        LazyRow {
            items(daysOfWeek.size) { index ->
                Box(
                    modifier = Modifier
                        .clickable {
                            val selectedDays = recurrenceWeekDaysHabit?.split(",")?.toMutableList() ?: mutableListOf()
                            if (selectedDays.contains(index.toString())) {
                                selectedDays.remove(index.toString())
                            } else {
                                selectedDays.add(index.toString())
                            }
                            appViewModel.changeRecurrenceWeekDaysHabit(selectedDays.joinToString(","))
                        }
                        .background(if (recurrenceWeekDaysHabit?.contains(index.toString()) == true) Color.Magenta else themeColors.backGround1)
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
