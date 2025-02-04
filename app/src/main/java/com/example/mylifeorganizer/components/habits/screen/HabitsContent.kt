package com.example.mylifeorganizer.components.habits.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.HabitEntity
import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Locale

// Función para verificar si una fecha está dentro de un rango
fun isDateInRange(selectedDate: String, dateRange: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val (startDateStr, endDateStr) = dateRange.split("-")
    val selectedDateObj = dateFormat.parse(selectedDate)
    val startDateObj = dateFormat.parse(startDateStr)
    val endDateObj = dateFormat.parse(endDateStr)

    return selectedDateObj in startDateObj..endDateObj
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitsContent(
    selectedDate: String,
    habits: List<HabitEntity>,
    habitsOccurrences: List<HabitOccurrenceEntity>
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Filtrar las ocurrencias para el día seleccionado
    val occurrencesForDate = habitsOccurrences.filter { occurrence ->
        if (occurrence.date.contains("-")) {
            // Es un rango de fechas
            isDateInRange(selectedDate, occurrence.date)
        } else {
            // Es una fecha única
            occurrence.date == selectedDate
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (occurrencesForDate.isEmpty()) {
            item {
                    Text(
                    text = "No hay hábitos para este día.",
                    color = themeColors.text1,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        } else {
            occurrencesForDate.forEach { occurrence ->
                val habit = habits.first { it.habitId == occurrence.habitId }

                item {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(vertical = 2.dp)
                    ) {
                        // Primera columna: Botón para marcar como completado
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.1f)
                                .fillMaxHeight()
                                .background(themeColors.backGround3)
                                .clickable {
                                    // Actualizar isCompleted en la base de datos
                                    noteViewModel.updateHabitOccurrence(
                                        occurrence.copy(isCompleted = !occurrence.isCompleted)
                                    )
                                },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (occurrence.isCompleted) {
                                        R.drawable.baseline_check_box_24
                                    } else {
                                        R.drawable.baseline_check_box_outline_blank_24
                                    }
                                ),
                                contentDescription = null,
                                tint = if (occurrence.isCompleted) {
                                    Color.Green
                                } else {
                                    Color.Gray
                                }
                            )
                        }

                        Column (
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight()
                                .background(
                                    color = themeViewModel.getCategoryColor(habit.color)
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = habit.title,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = occurrence.date,
                                color = themeColors.text1,
                                fontSize = 12.sp
                            )
                        }

                        // Tercera columna: Fecha o progreso
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .fillMaxHeight()
                                .background(themeColors.backGround3)
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (occurrence.date.contains("-")) {
                                // Es un rango de fechas
                                val totalTasks = when (habit.recurrencePattern) {
                                    "weekly" -> habit.numDaysForWeekly
                                    "monthly" -> habit.numDaysForMonthly
                                    "yearly" -> habit.numDaysForYearly
                                    else -> 1
                                }
                                val completedTasks = habitsOccurrences.count {
                                    it.habitId == habit.habitId && it.isCompleted && it.date.contains("-")
                                }

                                // Mostrar el progreso de tareas completadas
                                Text(
                                    text = "$completedTasks/$totalTasks",
                                    color = themeColors.text1,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )

                            } else {
                                // Es una fecha única
                                Text(
                                    text = occurrence.date,
                                    color = themeColors.text1,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = habit.doItAt,
                                    color = themeColors.text1,
                                    fontSize = 14.sp
                                )

                                Text(
                                    text = when {
                                        habit.doItAt == "any" -> "\uD83C\uDF1E \uD83C\uDF19"
                                        habit.doItAt == "morning" -> "\uD83C\uDF05"
                                        habit.doItAt == "afternoon" -> "\uD83C\uDF04"
                                        habit.doItAt == "night" -> "\uD83C\uDF06"
                                        else -> {
                                            val timeParts = habit.doItAt.split(":").mapNotNull { it.toIntOrNull() }
                                            if (timeParts.size == 2) {
                                                val hour = timeParts[0]
                                                when {
                                                    hour in 5..11 -> "\uD83C\uDF05" // Mañana
                                                    hour in 12..19 -> "\uD83C\uDF04" // Tarde
                                                    hour in 20..23 || hour in 0..4 -> "\uD83C\uDF06" // Noche
                                                    else -> habit.doItAt
                                                }
                                            } else {
                                                habit.doItAt
                                            }
                                        }
                                    },
                                    color = themeColors.text1,
                                    fontSize = 14.sp
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}