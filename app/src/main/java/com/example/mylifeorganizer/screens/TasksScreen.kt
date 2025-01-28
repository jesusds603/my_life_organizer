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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.tasks.create.CategoryDialogTask
import com.example.mylifeorganizer.components.tasks.create.CustomDatePicker
import com.example.mylifeorganizer.components.tasks.create.CustomTimePicker
import com.example.mylifeorganizer.components.tasks.create.MainWindowDialog
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
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
    val showDatePicker = appViewModel.showDatePicker.value
    val showTimePicker = appViewModel.showTimePicker.value

    var selectedDate by remember { mutableStateOf(LocalDate.now()) } // Fecha seleccionada
    var selectedTime by remember { mutableStateOf(Pair(12, 0)) } // Hora seleccionada (hora, minuto)


    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryColor by remember { mutableStateOf("") }

    var isRecurring by remember { mutableStateOf(false) }
    var recurrencePattern by remember { mutableStateOf("Off") }
    var recurrenceInterval by remember { mutableStateOf(0) }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {

        }

        // Botón flotante para agregar nuevas tareas
        FloatingActionButton(
            onClick = { appViewModel.toggleShowDialogCreateTask() },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Ubicación en la esquina inferior derecha
                .padding(16.dp),
            containerColor = themeColors.buttonAdd,
            shape = CircleShape
        ) {
            Text("+", fontSize = 24.sp, color = themeColors.text1)
        }

        // Ventana de diálogo para agregar tareas
        if (showDialogCreateTask) {
            MainWindowDialog(
                isRecurring = isRecurring,
                onIsRecurring = { isRecurring = it },
                recurrencePattern = recurrencePattern,
                onRecurrencePattern = { recurrencePattern = it },
                recurrenceInterval = recurrenceInterval,
                onRecurrenceInterval = { recurrenceInterval = it }
            )
        }

        if(showDatePicker) {
            CustomDatePicker(
                initialDate = selectedDate,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                },
            )
        }

        if(showTimePicker) {
            CustomTimePicker(
                initialTime = selectedTime,
                onTimeSelected = { hour, minute ->
                    selectedTime = Pair(hour, minute)
                },

            )
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


