package com.example.mylifeorganizer.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.tasks.create.CategoryDialogTask
import com.example.mylifeorganizer.components.tasks.create.CustomDatePicker
import com.example.mylifeorganizer.components.tasks.create.CustomTimePicker
import com.example.mylifeorganizer.components.tasks.create.MainWindowDialog
import com.example.mylifeorganizer.components.tasks.screen.MainContent
import com.example.mylifeorganizer.components.tasks.screen.RowCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksScreen () {
    val appViewModel: AppViewModel = viewModel()

    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Estado para controlar la visibilidad del cuadro de diálogo
    val showDialogCreateTask = appViewModel.showDialogCreateTask.value
    val showCreateCategoryDialogTask = appViewModel.showCreateCategoryDialogTask.value
    val isEditingTask = appViewModel.isEditingTask.value
    val showDatePicker = appViewModel.showDatePicker.value
    val showTimePicker = appViewModel.showTimePicker.value

    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryColor by remember { mutableStateOf("") }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

       Column (
           modifier = Modifier.fillMaxSize()
       ) {
           // Fila superior
           RowCategories()

           MainContent()
       }

        // Botón flotante para agregar nuevas tareas
        FloatingActionButton(
            onClick = {
                appViewModel.changeTitleNewTask("")
                appViewModel.changeDescriptionNewTask("")
                appViewModel.updateSelectedDueDate("")
                appViewModel.updateSelectedDueTime("")
                appViewModel.updateSelectedCategoriesTask(emptyList())
                appViewModel.updatePriorityNewTask( 1)
                appViewModel.toggleIsTaskRecurring()
                appViewModel.updateRecurrenceTaskPattern("")
                appViewModel.updateNumDaysNewTask(0)
                appViewModel.updateSelectedWeekDaysNewTask( emptyList() )
                appViewModel.updateNumWeeksNewTask( 0)
                appViewModel.updateSelectedMonthDaysNewTask( emptyList() )
                appViewModel.updateNumMonthsNewTask(0)
                appViewModel.updateSelectedYearDaysNewTask( emptySet() )
                appViewModel.updateNumYearsNewTask(0)
                appViewModel.updateSelectedCustomIntervalNewTask(1)
                appViewModel.updateNumTimesNewTask(0)

                appViewModel.toggleShowDialogCreateTask()
                      },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Ubicación en la esquina inferior derecha
                .padding(16.dp),
            containerColor = themeColors.buttonAdd,
            shape = CircleShape
        ) {
            Text("+", fontSize = 24.sp, color = themeColors.text1)
        }

        // Ventana de diálogo para agregar tareas
        if (showDialogCreateTask || isEditingTask) {
            MainWindowDialog()
        }

        if(showDatePicker) {
            CustomDatePicker()
        }

        if(showTimePicker) {
            CustomTimePicker()
        }

        if(showCreateCategoryDialogTask) {
            CategoryDialogTask(
                newCategoryName = newCategoryName,
                onNewCategoryName = { newCategoryName = it },
                newCategoryColor = newCategoryColor,
                onNewCategoryColor = { newCategoryColor = it }
            )
        }
    }
}


