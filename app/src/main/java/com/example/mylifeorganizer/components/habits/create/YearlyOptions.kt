package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
fun YearlyOptions() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val isYearlyAnytimeHabit = appViewModel.isYearlyAnytimeHabit.value
    val numDaysForYearlyHabit = appViewModel.numDaysForYearlyHabit.value
    val recurrenceYearDaysHabit = appViewModel.recurrenceYearDaysHabit.value

    // Lista de días específicos
    val specificDays = listOf(1, 2, 3, 4, 5, 7, 10, 14, 15, 20, 25, 30, 50, 60, 90, 100, 120, 150, 190, 200, 210, 240, 270, 300, 330, 360, 365)

    // Lista de tripletas (MMM, MM, días)
    val monthsWithDays = listOf(
        Triple("Jan", "01", 31),
        Triple("Feb", "02", 29), // Febrero con 29 días
        Triple("Mar", "03", 31),
        Triple("Apr", "04", 30),
        Triple("May", "05", 31),
        Triple("Jun", "06", 30),
        Triple("Jul", "07", 31),
        Triple("Aug", "08", 31),
        Triple("Sep", "09", 30),
        Triple("Oct", "10", 31),
        Triple("Nov", "11", 30),
        Triple("Dec", "12", 31)
    )

    // Estado para el mes seleccionado
    var selectedMonth by remember { mutableStateOf<Triple<String, String, Int>?>(null) }

    // Estado para los días seleccionados en formato MM/dd
    val selectedDays = recurrenceYearDaysHabit.split(",").toMutableList()

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
            items(specificDays) { day ->
                Box(
                    modifier = Modifier
                        .clickable { appViewModel.changeNumDaysYearlyHabit(day) }
                        .background(if (numDaysForYearlyHabit == day) Color.Magenta else themeColors.backGround1)
                        .padding(8.dp)
                ) {
                    Text(
                        text = day.toString(),
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
            items(monthsWithDays) { monthTriplet ->
                val (monthName, _, _) = monthTriplet
                Box(
                    modifier = Modifier
                        .clickable { selectedMonth = monthTriplet }
                        .background(if (selectedMonth == monthTriplet) Color.Magenta else themeColors.backGround1)
                        .padding(8.dp)
                ) {
                    Text(
                        text = monthName,
                        color = themeColors.text1
                    )
                }
            }
        }

        // Fila 2: Mostrar días del mes seleccionado
        selectedMonth?.let { month ->
            val (_, monthNumber, daysInMonth) = month
            LazyRow {
                items(daysInMonth) { day ->
                    val dayOfMonth = day + 1
                    val formattedDay = "$monthNumber/${dayOfMonth.toString().padStart(2, '0')}"
                    Box(
                        modifier = Modifier
                            .clickable {
                                // Convertir recurrenceYearDaysHabit a lista mutable
                                val daysList = recurrenceYearDaysHabit.split(",").toMutableList()

                                if (daysList.contains(formattedDay)) {
                                    daysList.remove(formattedDay) // Eliminar si ya existe
                                } else {
                                    daysList.add(formattedDay) // Agregar si no existe
                                }
                                // Actualizar el valor en el ViewModel
                                appViewModel.changeRecurrenceYearDaysHabit(daysList.joinToString(","))
                            }
                            .background(if (recurrenceYearDaysHabit.contains(formattedDay)) Color.Magenta else themeColors.backGround1)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = dayOfMonth.toString(),
                            color = themeColors.text1
                        )
                    }
                }
            }
        }
        Text(
            text = "Selected days:",
            color = themeColors.text1
        )

        // Mostrar los días seleccionados
        LazyRow {
            recurrenceYearDaysHabit.forEach { day ->
                item {
                    Text(
                        text = "$day",
                        color = themeColors.text1
                    )
                }
            }
        }
    }
}