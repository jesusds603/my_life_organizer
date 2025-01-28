package com.example.mylifeorganizer.components.tasks.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun ContentMainWindow(
    selectedCategories: List<CategoryTaskEntity>,
    onSelectedCategories: (List<CategoryTaskEntity>) -> Unit,
    availableCategories: List<CategoryTaskEntity>,
    title: String,
    onTitle: (String) -> Unit,
    description: String,
    onDescription: (String) -> Unit,
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


    Column {
        // Campo de título
        TextField(
            value = title,
            onValueChange = { onTitle(it) },
            label = { Text("Title", color = themeColors.text1) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeColors.backGround2,
                unfocusedContainerColor = themeColors.backGround2,
                cursorColor = Color.Magenta
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de descripción
        TextField(
            value = description,
            onValueChange = { onDescription(it) },
            label = { Text("Description", color = themeColors.text1) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeColors.backGround2,
                unfocusedContainerColor = themeColors.backGround2,
                cursorColor = Color.Magenta
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        RowDateTimePickers(
            onShowDatePicker = onShowDatePicker,
            onShowTimePicker = onShowTimePicker,
            dueDate = dueDate,
            dueTime = dueTime
        )

        Spacer(modifier = Modifier.height(8.dp))

        PriorityTask(
            priority = priority,
            onPriority = onPriority
        )

        Spacer(modifier = Modifier.height(8.dp))


        Recurrence(
            isRecurring = isRecurring,
            onIsRecurring = onIsRecurring,
            recurrencePattern = recurrencePattern,
            onRecurrencePattern = onRecurrencePattern,
            recurrenceInterval = recurrenceInterval,
            onRecurrenceInterval = onRecurrenceInterval,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Seleccionar categorías
        SelectCategories(
            selectedCategories = selectedCategories,
            onSelectedCategories = onSelectedCategories,
            availableCategories = availableCategories,
            onShowCreateCategoryDialog = onShowCreateCategoryDialog
        )

    }
}