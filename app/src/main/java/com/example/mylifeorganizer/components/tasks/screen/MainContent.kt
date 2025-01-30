package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
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

    val taskIdSelectedScreen = appViewModel.taskIdSelectedScreen



    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DropdownMenu(
            expanded = taskIdSelectedScreen != null,
            onDismissRequest = { appViewModel.updateTaskIdSelectedScreen(null) }, // Cierra el menú al hacer clic afuera
            modifier = Modifier
                .background(themeColors.backGround1)
                .border(width = 1.dp, color = themeColors.text3),
            offset = DpOffset(x = 0.dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                text = { Text(
                    text= "Edit Task",
                    color = themeColors.text1
                ) },
                onClick = {
                    appViewModel.changeTitleNewTask(taskMap[taskIdSelectedScreen]?.task?.title ?: "")
                    appViewModel.changeDescriptionNewTask(taskMap[taskIdSelectedScreen]?.task?.description ?: "")
                    appViewModel.updateSelectedDueDate(taskMap[taskIdSelectedScreen]?.task?.dueDate ?: "")
                    appViewModel.updateSelectedDueTime(taskMap[taskIdSelectedScreen]?.task?.dueTime ?: "")
                    appViewModel.updateSelectedCategoriesTask(taskMap[taskIdSelectedScreen]?.categories ?: emptyList())
                    appViewModel.updatePriorityNewTask(taskMap[taskIdSelectedScreen]?.task?.priority ?: 1)
                    appViewModel.toggleIsTaskRecurring()
                    appViewModel.updateRecurrenceTaskPattern(taskMap[taskIdSelectedScreen]?.task?.recurrencePattern ?: "")
                    appViewModel.updateNumDaysNewTask(taskMap[taskIdSelectedScreen]?.task?.numDays ?: 0)
                    appViewModel.updateSelectedWeekDaysNewTask(
                        taskMap[taskIdSelectedScreen]?.task?.recurrenceWeekDays
                            ?.split(",")  // Divide la cadena en una lista de Strings
                            ?.mapNotNull { it.toIntOrNull() } // Convierte cada elemento a Int, ignorando errores
                            ?: emptyList() // Si es null, devuelve una lista vacía
                    )
                    appViewModel.updateNumWeeksNewTask(taskMap[taskIdSelectedScreen]?.task?.numWeeks ?: 0)
                    appViewModel.updateSelectedMonthDaysNewTask(
                        taskMap[taskIdSelectedScreen]?.task?.recurrenceMonthDays
                            ?.split(",")  // Divide la cadena en una lista de Strings
                            ?.mapNotNull { it.toIntOrNull() } // Convierte cada elemento a Int, ignorando errores
                            ?: emptyList()
                    )
                    appViewModel.updateNumMonthsNewTask(taskMap[taskIdSelectedScreen]?.task?.numMonths ?: 0)
                    appViewModel.updateSelectedYearDaysNewTask(
                        taskMap[taskIdSelectedScreen]?.task?.recurrenceYearDays
                            ?.split(",")  // Divide la cadena en una lista de Strings
                            ?.toSet() ?: emptySet() // Convierte a Set
                    )
                    appViewModel.updateNumYearsNewTask(taskMap[taskIdSelectedScreen]?.task?.numYears ?: 0)
                    appViewModel.updateSelectedCustomIntervalNewTask(taskMap[taskIdSelectedScreen]?.task?.recurrenceInterval ?: 1)
                    appViewModel.updateNumTimesNewTask(taskMap[taskIdSelectedScreen]?.task?.numTimes ?: 0)

                    appViewModel.updateTaskIdSelectedScreen(null)
                    appViewModel.toggleShowDialogCreateTask()
                }
            )

            DropdownMenuItem(
                text = { Text(
                    text= "Delete Task",
                    color = themeColors.text1
                ) },
                onClick = {
                    appViewModel.updateTaskIdSelectedScreen(null)
                    noteViewModel.deleteTaskWithOccurrences(taskId = taskIdSelectedScreen!!)
                }
            )
        }


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
                    // Parse the date string to LocalDate
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                    val localDate = LocalDate.parse(date, dateFormatter)

                    // Get the day of the week (e.g., Monday, Tuesday)
                    val dayOfWeek = localDate.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }

                    // Calculate the difference in days between the current date and the task date
                    val currentDateText = LocalDate.now()
                    val daysDifference = currentDateText.until(localDate).days

                    // Determine the text to display based on the difference in days
                    val daysText = when {
                        daysDifference == 0 -> "Today"
                        daysDifference > 0 -> "in $daysDifference days"
                        else -> "past ${-daysDifference} days"
                    }

                    // Mostrar la fecha como un separador
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = date,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dayOfWeek,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = daysText,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
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
    }


}
