package com.example.mylifeorganizer.components.habits.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.HabitEntity
import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header(
    selectedDate: String,
    habitsOccurrences: List<HabitOccurrenceEntity>
) {
    val appViewModel: AppViewModel = viewModel()
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

    // Agrupar ocurrencias por hábito para evitar duplicados en rangos
    val groupedOccurrences = occurrencesForDate.groupBy { it.habitId }

    val completedOccurrences = groupedOccurrences.filter { (_, occurrences) ->
        occurrences.all { it.isCompleted }
    }
    val uncompletedOccurrences = groupedOccurrences.filter { (_, occurrences) ->
        occurrences.any { !it.isCompleted }
    }

    val numCompleted = completedOccurrences.size
    val numUncompleted = uncompletedOccurrences.size
    val total = numCompleted + numUncompleted
    val percentageDone = if (total > 0) Math.round((numCompleted.toFloat() / total.toFloat()) * 100) else 0f

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color.Magenta,
                    start = Offset(0f, size.height - strokeWidth / 2),
                    end = Offset(size.width, size.height - strokeWidth / 2),
                    strokeWidth = strokeWidth
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = selectedDate,
                color = themeColors.text1,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$percentageDone% Finished",
                color = themeColors.text1,
                fontSize = 16.sp
            )
        }

        FloatingActionButton(
            onClick = {
                appViewModel.toggleAddingHabit()
            },
            modifier = Modifier
                .padding(8.dp),
            containerColor = themeColors.buttonAdd,
            shape = CircleShape
        ) {
            Text("+", fontSize = 32.sp, color = themeColors.text1)
        }
    }
}