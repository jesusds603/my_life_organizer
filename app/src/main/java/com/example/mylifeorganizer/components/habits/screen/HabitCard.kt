package com.example.mylifeorganizer.components.habits.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitCard(
    habit: HabitEntity,
    isRange: Boolean,
    habitsOccurrences: List<HabitOccurrenceEntity>,
    occurrences: List<HabitOccurrenceEntity>,
    habitId: Long
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 2.dp)
    ) {
        // Primera columna: Flechas para aumentar/disminuir tareas completadas
        Column(
            modifier = Modifier
                .fillMaxWidth(0.1f)
                .fillMaxHeight()
                .background(themeColors.backGround3),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isRange) {
                // Mostrar flechas solo para rangos
                val totalTasks = when (habit.recurrencePattern) {
                    "weekly" -> habit.numDaysForWeekly
                    "monthly" -> habit.numDaysForMonthly
                    "yearly" -> habit.numDaysForYearly
                    else -> 1
                }
                val completedTasks = habitsOccurrences.count {
                    it.habitId == habitId && it.isCompleted && it.date.contains("-")
                }

                // Flecha hacia arriba
                IconButton(
                    onClick = {
                        if (completedTasks < totalTasks) {
                            // Encontrar la primera ocurrencia no completada y marcarla como completada
                            val occurrenceToUpdate = occurrences.firstOrNull { !it.isCompleted }
                            occurrenceToUpdate?.let {
                                noteViewModel.updateHabitOccurrence(it.copy(isCompleted = true))
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                        contentDescription = "Aumentar",
                        tint = if (completedTasks < totalTasks) themeColors.text1 else Color.Gray
                    )
                }

                // Flecha hacia abajo
                IconButton(
                    onClick = {
                        if (completedTasks > 0) {
                            // Encontrar la última ocurrencia completada y marcarla como no completada
                            val occurrenceToUpdate = occurrences.lastOrNull { it.isCompleted }
                            occurrenceToUpdate?.let {
                                noteViewModel.updateHabitOccurrence(it.copy(isCompleted = false))
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                        contentDescription = "Disminuir",
                        tint = if (completedTasks > 0) themeColors.text1 else Color.Gray
                    )
                }
            } else {
                // Mostrar el icono de selección para fechas únicas
                IconButton(
                    onClick = {
                        val occurrence = occurrences.first()
                        noteViewModel.updateHabitOccurrence(occurrence.copy(isCompleted = !occurrence.isCompleted))
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (occurrences.first().isCompleted) {
                                R.drawable.baseline_check_box_24
                            } else {
                                R.drawable.baseline_check_box_outline_blank_24
                            }
                        ),
                        contentDescription = null,
                        tint = if (occurrences.first().isCompleted) Color.Green else Color.Gray
                    )
                }
            }
        }

        // Segunda columna: Título y fecha
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight()
                .background(
                    color = themeViewModel.getCategoryColor(habit.color)
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = habit.title,
                color = themeColors.text1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isRange) "Rango: ${occurrences.first().date}" else occurrences.first().date,
                color = themeColors.text1,
                fontSize = 12.sp
            )
        }

        // Tercera columna: Progreso o fecha
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight()
                .background(themeColors.backGround3)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isRange) {
                // Mostrar el progreso de tareas completadas
                val totalTasks = when (habit.recurrencePattern) {
                    "weekly" -> habit.numDaysForWeekly
                    "monthly" -> habit.numDaysForMonthly
                    "yearly" -> habit.numDaysForYearly
                    else -> 1
                }
                val completedTasks = habitsOccurrences.count {
                    it.habitId == habitId && it.isCompleted && it.date.contains("-")
                }

                Text(
                    text = "$completedTasks/$totalTasks",
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = occurrences.first().time,
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            } else {
                // Mostrar la fecha única
                Text(
                    text = occurrences.first().time,
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}