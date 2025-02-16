package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.TaskOccurrenceEntity
import com.example.mylifeorganizer.room.TaskWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ColumnTasks(
    selectedDueOrCompleted: String,
    pendingOcurrences: Map<String, List<Pair<TaskOccurrenceEntity, TaskWithCategories?>>>,
    completedOcurrences: Map<String, List<Pair<TaskOccurrenceEntity, TaskWithCategories?>>>,
    notDoneOcurrences: Map<String, List<Pair<TaskOccurrenceEntity, TaskWithCategories?>>>,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value


    LazyColumn  (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 2.dp)
    ) {

        // Seleccionar las tareas a mostrar (pendientes o completadas)
        val ocurrencesToShow = when(selectedDueOrCompleted) {
            "due" -> pendingOcurrences
            "completed" -> completedOcurrences
            "notDone" -> notDoneOcurrences
            else -> pendingOcurrences
        }

        // Iterar sobre los grupos de tareas por día
        ocurrencesToShow.forEach { (date, occurrences) ->
            // Parse the date string to LocalDate
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            val localDate = LocalDate.parse(date, dateFormatter)

            // Get the day of the week (e.g., Monday, Tuesday)
            val dayOfWeek = localDate.dayOfWeek.toString()
                .lowercase().replaceFirstChar { it.uppercase() }

            // Calculate the difference in days between the current date and the task date
            val currentDateText = LocalDate.now()
            val daysDifference = ChronoUnit.DAYS.between(currentDateText, localDate)

            // Determine the text to display based on the difference in days
            val daysText = when {
                daysDifference == 0L -> if(isLangEng) "Today" else "Hoy"
                daysDifference > 0 -> if(isLangEng) "in ${daysDifference} days" else "en ${daysDifference} días"
                else -> if (isLangEng) "past ${-daysDifference} days" else "hace ${-daysDifference} días"
            }

            // Mostrar la fecha como un separador
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = themeColors.bgCardFolder)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = date,
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = dayOfWeek,
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = daysText,
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            }

            val sortedOccurrencesByHour = occurrences.sortedBy { it.first.dueTime }

            // Mostrar las tareas para este día
            items(sortedOccurrencesByHour) { occurrence ->
                TaskCard(occurrence = occurrence)
            }
        }
    }
}