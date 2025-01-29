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
    val tasksWithCategories = noteViewModel.tasksWithCategories.collectAsState(initial = emptyList()).value

    val nameSelectedCategoryTasksScreen = appViewModel.nameSelectedCategorieTasksScreen

    // Filtrar categorías por nombre
    val filteredTasks = if(nameSelectedCategoryTasksScreen == "All") {
        tasksWithCategories
    } else {
        tasksWithCategories.filter { task ->
            task.categories.any { category ->
                category.name == nameSelectedCategoryTasksScreen
            }
        }
    }

    // Obtener la fecha actual
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) // Formato: "yyyy/MM/dd"

    println("currentDate: $currentDate")

    val sortedTasks = filteredTasks.sortedBy { it.task.dueDateDay }


    // Agrupar tareas por día
    val tasksGroupedByDay = sortedTasks.groupBy { it.task.dueDateDay }

    // Separar tareas no hechas (fecha anterior a la actual y no completadas)
    val notDoneTasks = tasksGroupedByDay.mapValues { (date, tasks) ->
        tasks.filter { task ->
            !task.task.isCompleted && task.task.dueDateDay < currentDate
        }
    }.filterValues { it.isNotEmpty() }

    // Separar tareas pendientes (fecha mayor o igual a la actual y no completadas)
    val pendingTasks = tasksGroupedByDay.mapValues { (date, tasks) ->
        tasks.filter { task ->
            !task.task.isCompleted && task.task.dueDateDay >= currentDate
        }
    }.filterValues { it.isNotEmpty() }

    val completedTasks = tasksGroupedByDay.mapValues { (_, tasks) ->
        tasks.filter { it.task.isCompleted }
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
            val tasksToShow = when(selectedDueOrCompleted) {
                "due" -> pendingTasks
                "completed" -> completedTasks
                "notDone" -> notDoneTasks
                else -> pendingTasks
            }

            // Iterar sobre los grupos de tareas por día
            tasksToShow.forEach { (date, tasks) ->
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

                val sortedTasksByHour = tasks.sortedBy { it.task.dueDateTime }

                // Mostrar las tareas para este día
                items(sortedTasksByHour) { task ->
                    TaskCard(task = task)
                }
            }
        }
    }


}
