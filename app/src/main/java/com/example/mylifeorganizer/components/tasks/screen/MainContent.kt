package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.TaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val occurrenceTasks = noteViewModel.getAllOccurrences().collectAsState(initial = emptyList()).value
    val tasksWithCategories = noteViewModel.tasksWithCategories.collectAsState(initial = emptyList()).value

    val nameSelectedCategoryTasksScreen = appViewModel.nameSelectedCategorieTasksScreen

    // Create a map of taskId to TaskWithCategories for quick lookup
    val taskMap = tasksWithCategories.associateBy { it.task.taskId }

    // Associate each occurrence with its task
    val occurrencesWithTasks = occurrenceTasks.map { occurrence ->
        occurrence to taskMap[occurrence.taskId]
    }


    // Filtrar categorías por nombre
    val filteredOccurrences = if(nameSelectedCategoryTasksScreen == "All") {
        occurrencesWithTasks
    } else {
        occurrencesWithTasks.filter { (occurrence, task) ->
            task?.categories?.any { category ->
                category.name == nameSelectedCategoryTasksScreen
            } ?: false
        }
    }

    // Obtener la fecha actual
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) // Formato: "yyyy/MM/dd"

//    println("currentDate: $currentDate")

    val sortedOcurrences = filteredOccurrences.sortedBy { it.first.dueDate }


    // Agrupar tareas por día
    val ocurrencesGroupedByDay = sortedOcurrences.groupBy { it.first.dueDate }

    // Separar tareas no hechas (fecha anterior a la actual y no completadas)
    val notDoneOcurrences = ocurrencesGroupedByDay.mapValues { (date, ocurrences) ->
        ocurrences.filter { ocurrence ->
            !ocurrence.first.isCompleted && ocurrence.first.dueDate < currentDate
        }
    }.filterValues { it.isNotEmpty() }

    // Separar tareas pendientes (fecha mayor o igual a la actual y no completadas)
    val pendingOcurrences = ocurrencesGroupedByDay.mapValues { (date, ocurrences) ->
        ocurrences.filter { ocurrence ->
            !ocurrence.first.isCompleted && ocurrence.first.dueDate >= currentDate
        }
    }.filterValues { it.isNotEmpty() }

    val completedOcurrences = ocurrencesGroupedByDay.mapValues { (_, ocurrences) ->
        ocurrences.filter { it.first.isCompleted }
    }.filterValues { it.isNotEmpty() }



    var selectedDueOrCompleted by remember { mutableStateOf("due") } // "due" or "completed" or "notDone"


    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .background(color = themeColors.backGround3),
                onClick = {
                    selectedDueOrCompleted = "due"
                }
            ) {
                Text(
                    text = "Pending",
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .background(color = themeColors.backGround3),
                onClick = {
                    selectedDueOrCompleted = "completed"
                }
            ) {
                Text(
                    text = "Completed",
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .background(color = themeColors.backGround3),
                onClick = {
                    selectedDueOrCompleted = "notDone"
                }
            ) {
                Text(
                    text = "Not Done",
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold
                )
            }
        }

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
                // Mostrar la fecha como un separador
                item {
                    Text(
                        text = date,
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
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


}
