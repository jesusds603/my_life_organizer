package com.example.mylifeorganizer.components.habits.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
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
    habitsOccurrences: List<HabitOccurrenceEntity>
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val noteViewModel: NoteViewModel = appViewModel.noteViewModel

    val habits = noteViewModel.habits.collectAsState(initial = emptyList()).value

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

    // Agrupar ocurrencias por hábito para evitar duplicados en rangos
    val groupedOccurrences = occurrencesForDate.groupBy { it.habitId }

    val completedOccurrences = groupedOccurrences.filter { (_, occurrences) ->
        occurrences.all { it.isCompleted }
    }
    val uncompletedOccurrences = groupedOccurrences.filter { (_, occurrences) ->
        occurrences.any { !it.isCompleted }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (groupedOccurrences.isEmpty()) {
            item {
                Text(
                    text = "No hay hábitos para este día.",
                    color = themeColors.text1,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        } else {
            item {
                Text(
                    text = "No completados",
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
            uncompletedOccurrences.forEach { (habitId, uncomplOccurrences) ->
                val habit = habits.firstOrNull { it.habitId == habitId }
                val isRange = uncomplOccurrences.any { it.date.contains("-") }
                val occurrenceForHabit = uncomplOccurrences.first { uncOcc ->
                    uncOcc.habitId == habitId
                }

                if(habit != null) {
                    item {
                        HabitCard(
                            habit = habit,
                            occurrence = occurrenceForHabit,
                            isRange = isRange,
                            occurrences = habitsOccurrences,
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Completados",
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
            completedOccurrences.forEach { (habitId, complOccurrences) ->
                val habit = habits.firstOrNull { it.habitId == habitId }
                val isRange = complOccurrences.any { it.date.contains("-") }
                val occurrenceForHabit = complOccurrences.first { complOcc ->
                    complOcc.habitId == habitId
                }

                if(habit != null) {
                    item {
                        HabitCard(
                            habit = habit,
                            occurrence = occurrenceForHabit,
                            isRange = isRange,
                            occurrences = habitsOccurrences,
                        )
                    }
                }
            }


        }
    }
}