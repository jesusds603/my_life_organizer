package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.TaskWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptionsTask(
    tasksWithCategories: List<TaskWithCategories>,
    changeShowDialogDeleteTask: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val taskMap = tasksWithCategories.associateBy { it.task.taskId }
    val taskIdSelectedScreen = appViewModel.taskIdSelectedScreen


    DropdownMenu(
        expanded = taskIdSelectedScreen != null,
        onDismissRequest = { appViewModel.updateTaskIdSelectedScreen(null) }, // Cierra el menú al hacer clic afuera
        modifier = Modifier
            .background(themeColors.bgDialog)
            .padding(8.dp)
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

                appViewModel.toggleIsEditingTask()
            },
            modifier = Modifier
                .background(themeColors.backGround1)
        )

        DropdownMenuItem(
            text = { Text(
                text= "Delete Task",
                color = themeColors.text1
            ) },
            onClick = {
                changeShowDialogDeleteTask(true)
            },
            modifier = Modifier
                .background(themeColors.backGround1)
        )
    }
}