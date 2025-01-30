package com.example.mylifeorganizer.components.tasks.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.tasks.generateTaskOccurrences
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.room.TaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainWindowDialog() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val selectedDueDate = appViewModel.selectedDueDate
    val selectedDueTime = appViewModel.selectedDueTime

    val titleNewTask = appViewModel.titleNewTask.value
    val descriptionNewTask = appViewModel.descriptionNewTask.value
    val selectedCategoriesTask = appViewModel.selectedCategoriesTask
    val priorityNewTask = appViewModel.priorityNewTask

    val isTaskRecurring = appViewModel.isTaskRecurring  // Booleano para determinar si la tarea es recurrente
    val recurrenceTaskPattern = appViewModel.recurrenceTaskPattern // "daily", "weekly", "monthly", "yearly", "custom"
    val numDaysNewTask = appViewModel.numDaysNewTask  // Número de días para el daily
    val selectedWeekDaysNewTask = appViewModel.selectedWeekDaysNewTask // Indices de los días de la semana para el weekly
    val numWeeksNewTask = appViewModel.numWeeksNewTask // Número de semanas para el weekly
    val selectedMonthDaysNewTask = appViewModel.selectedMonthDaysNewTask // Numeros de los dias del mes para el monthly
    val numMonthsNewTask = appViewModel.numMonthsNewTask // Número de meses para el monthly
    val selectedYearDaysNewTask = appViewModel.selectedYearDaysNewTask // Fechas del año en formato "MM/DD" para el yearly
    val numYearsNewTask = appViewModel.numYearsNewTask // Número de años para el yearly
    val selectedCustomIntervalNewTask = appViewModel.selectedCustomIntervalNewTask // Entero para el numero de dias que se espacia
    val numTimesNewTask = appViewModel.numTimesNewTask // Numero de veces que se repite la tarea



    AlertDialog(
        onDismissRequest = { appViewModel.toggleShowDialogCreateTask() },
        confirmButton = {
            Button(
                onClick = {
                    noteViewModel.addTask(
                        task = TaskEntity(
                            title = titleNewTask,
                            description = descriptionNewTask,
                            dueDate = selectedDueDate,
                            dueTime = selectedDueTime,

                            isRecurring = isTaskRecurring,
                            recurrencePattern = recurrenceTaskPattern,
                            numDays = numDaysNewTask,
                            recurrenceWeekDays = selectedWeekDaysNewTask.joinToString(","),
                            numWeeks = numWeeksNewTask,
                            recurrenceMonthDays = selectedMonthDaysNewTask.joinToString(","),
                            numMonths = numMonthsNewTask,
                            recurrenceYearDays = selectedYearDaysNewTask.joinToString(","),
                            numYears = numYearsNewTask,
                            recurrenceInterval = selectedCustomIntervalNewTask,
                            numTimes = numTimesNewTask,

                            priority = priorityNewTask,

                        ),
                        onTaskAdded = { taskId ->
                            selectedCategoriesTask.forEach { categoryTaskEntity ->
                                noteViewModel.linkTaskWithCategory(
                                    categoryId = categoryTaskEntity.categoryId,
                                    taskId = taskId
                                )
                            }

                            val occurrences =  generateTaskOccurrences(
                                taskId = taskId,
                                dueDate = selectedDueDate,
                                dueTime = selectedDueTime,
                                isRecurring = isTaskRecurring,
                                recurrencePattern = recurrenceTaskPattern,
                                numDays = numDaysNewTask,
                                recurrenceWeekDays = selectedWeekDaysNewTask,
                                numWeeks = numWeeksNewTask,
                                recurrenceMonthDays = selectedMonthDaysNewTask,
                                numMonths = numMonthsNewTask,
                                recurrenceYearDays = selectedYearDaysNewTask,
                                numYears = numYearsNewTask,
                                recurrenceInterval = selectedCustomIntervalNewTask,
                                numTimes = numTimesNewTask
                            )

                            // Guardar las ocurrencias en la base de datos
                            occurrences.forEach { occurrence ->
                                noteViewModel.insertOccurrence(occurrence)
                            }
                        }
                    )
                    appViewModel.toggleShowDialogCreateTask()
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(text = "Add Task", color = themeColors.text1)
            }
        },
        dismissButton = {
            Button(
                onClick = { appViewModel.toggleShowDialogCreateTask() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(text = "Cancel", color = themeColors.text1)
            }
        },
        title = { Text(
            text = "Add New Task",
            color = themeColors.text1
        ) },
        text = {
            ContentMainWindow()
        },
        containerColor = themeColors.backGround3,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large // Bordes más redondeados

    )
}