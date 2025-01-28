package com.example.mylifeorganizer.components.tasks.create

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun MainWindowDialog(
    isRecurring: Boolean,
    onIsRecurring: (Boolean) -> Unit,
    recurrencePattern: String,
    onRecurrencePattern: (String) -> Unit,
    recurrenceInterval: Int,
    onRecurrenceInterval: (Int) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    AlertDialog(
        onDismissRequest = { appViewModel.toggleShowDialogCreateTask() },
        confirmButton = {
            Button(
                onClick = { appViewModel.toggleShowDialogCreateTask() },
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
            ContentMainWindow(
                isRecurring = isRecurring,
                onIsRecurring = onIsRecurring,
                recurrencePattern = recurrencePattern,
                onRecurrencePattern = onRecurrencePattern,
                recurrenceInterval = recurrenceInterval,
                onRecurrenceInterval = onRecurrenceInterval
                )
        },
        containerColor = themeColors.backGround3,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large // Bordes más redondeados

    )
}