package com.example.mylifeorganizer.components.tasks.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Recurrence() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val isTaskRecurring = appViewModel.isTaskRecurring
    val recurrenceTaskPattern = appViewModel.recurrenceTaskPattern
    val recurrenceTaskInterval = appViewModel.recurrenceTaskInterval


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                appViewModel.toggleIsTaskRecurring()
            }
    ) {
        Text(
            text = "Recurrence: $recurrenceTaskPattern",
            color = themeColors.text1,
        )

        if(isTaskRecurring) {
            LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(listOf("Off", "Daily", "Weekly", "Monthly", "Yearly", "Custom")) { recurrence ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(color = themeColors.backGround1)
                            .clickable {
                                appViewModel.updateRecurrenceTaskPattern(recurrence)
                            }
                    ) {
                        Text(
                            text = recurrence,
                            color = if(recurrence == recurrenceTaskPattern) {
                                themeColors.tabButtonSelected
                            } else {
                                themeColors.tabButtonDefault
                            }
                        )
                    }

                }
            }
        }

        if(recurrenceTaskPattern == "Custom") {
            Text(text = "Recurrence Interval (days): $recurrenceTaskInterval", color = themeColors.text1)
            var expanded by remember { mutableStateOf(false) }
            var selectedValue by remember { mutableStateOf(recurrenceTaskInterval) }

            Box {
                Button(onClick = { expanded = true }) {
                    Text(text = "Select Interval")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Generar números del 1 al 365
                    (1..365).forEach { number ->
                        DropdownMenuItem(
                            text = { Text(text = "$number") },
                            onClick = {
                                selectedValue = number
                                appViewModel.updateRecurrenceTaskInterval(number)
                                expanded = false
                            }
                        )
                    }
                }
            }

        }
    }
}