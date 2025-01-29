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

    val titleNewTask = appViewModel.titleNewTask.value
    val descriptionNewTask = appViewModel.descriptionNewTask.value
    val selectedDueDate = appViewModel.selectedDueDate
    val selectedDueTime = appViewModel.selectedDueTime
    val selectedCategoriesTask = appViewModel.selectedCategoriesTask
    val isTaskRecurring = appViewModel.isTaskRecurring
    val recurrenceTaskPattern = appViewModel.recurrenceTaskPattern
    val recurrenceTaskInterval = appViewModel.recurrenceTaskInterval
    val recurrenceTaskEndDate = appViewModel.recurrenceTaskEndDate
    val priorityNewTask = appViewModel.priorityNewTask


    AlertDialog(
        onDismissRequest = { appViewModel.toggleShowDialogCreateTask() },
        confirmButton = {
            Button(
                onClick = {
                    noteViewModel.addTask(
                        task = TaskEntity(
                            title = titleNewTask,
                            description = descriptionNewTask,
                            priority = priorityNewTask,
                            isRecurring = isTaskRecurring,
                            recurrencePattern = recurrenceTaskPattern,
                            recurrenceInterval = recurrenceTaskInterval,
                            recurrenceEndDate = recurrenceTaskEndDate,
                        ),
                        onTaskAdded = { taskId ->
                            selectedCategoriesTask.forEach { categoryTaskEntity ->
                                noteViewModel.linkTaskWithCategory(
                                    categoryId = categoryTaskEntity.categoryId,
                                    taskId = taskId
                                )
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