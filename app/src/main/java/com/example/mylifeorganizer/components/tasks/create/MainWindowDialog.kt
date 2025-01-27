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
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun MainWindowDialog(
    selectedCategories: List<CategoryTaskEntity>,
    onSelectedCategories: (List<CategoryTaskEntity>) -> Unit,
    availableCategories: List<CategoryTaskEntity>,
    title: String,
    onTitle: (String) -> Unit,
    description: String,
    onDescription: (String) -> Unit,
    onShowDialog: (Boolean) -> Unit,
    onShowCreateCategoryDialog: (Boolean) -> Unit,
    onShowDatePicker: (Boolean) -> Unit,
    onShowTimePicker: (Boolean) -> Unit,
    dueDate: Long,
    dueTime: Long,
    priority: Int,
    onPriority: (Int) -> Unit,
    isRecurring: Boolean,
    onIsRecurring: (Boolean) -> Unit,
    recurrencePattern: String,
    onRecurrencePattern: (String) -> Unit,
    recurrenceInterval: Int,
    onRecurrenceInterval: (Int) -> Unit,
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value


    AlertDialog(
        onDismissRequest = { onShowDialog(false) },
        confirmButton = {
            Button(
                onClick = { onShowDialog(false) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(text = "Add Task", color = themeColors.text1)
            }
        },
        dismissButton = {
            Button(
                onClick = { onShowDialog(false) },
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
                selectedCategories = selectedCategories,
                onSelectedCategories = onSelectedCategories,
                availableCategories = availableCategories,
                title = title,
                onTitle = onTitle,
                description = description,
                onDescription = onDescription,
                onShowCreateCategoryDialog = onShowCreateCategoryDialog,
                onShowDatePicker = onShowDatePicker,
                onShowTimePicker = onShowTimePicker,
                dueDate = dueDate,
                dueTime = dueTime,
                priority = priority,
                onPriority = onPriority,
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