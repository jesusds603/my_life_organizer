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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var showDetails by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Select the recurrence pattern",
            color = themeColors.text1
        )

        // LazyRow para los botones de recurrencia
        LazyRow {
            items(listOf("Daily", "Weekly", "Monthly", "Yearly")) { recurrenceType ->
                val isSelected = recurrencePatternForNewHabit == recurrenceType.lowercase()
                Button(
                    onClick = {
                        showDetails = true
                        appViewModel.changeRecurrencePatternForNewHabit(recurrenceType.lowercase())
                    },
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

        if(showDetails) {
            // Dependiendo de la selección, mostramos las opciones adicionales
            when (recurrencePatternForNewHabit) {
                "weekly" -> {
                    WeeklyOptions()
                }
                "monthly" -> {
                    MonthlyOptions()
                }
                "yearly" -> {
                    YearlyOptions()
                }
            }

            Button(
                onClick = { showDetails = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Magenta
                )
            ) {
                Text(
                    text = "OK",
                    color = themeColors.text1
                )
            }
        }
    }
}

