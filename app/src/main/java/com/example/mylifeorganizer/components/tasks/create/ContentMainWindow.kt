package com.example.mylifeorganizer.components.tasks.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentMainWindow() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val titleNewTask = appViewModel.titleNewTask.value
    val descriptionNewTask = appViewModel.descriptionNewTask.value



    Column {
        // Campo de título
        TextField(
            value = titleNewTask,
            onValueChange = { appViewModel.changeTitleNewTask(it) },
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
            value = descriptionNewTask,
            onValueChange = { appViewModel.changeDescriptionNewTask(it) },
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

        RowDateTimePickers()

        Spacer(modifier = Modifier.height(8.dp))

        PriorityTask()

        Spacer(modifier = Modifier.height(8.dp))


        Recurrence()

        Spacer(modifier = Modifier.height(8.dp))

        // categorías
        CategoriesTasksSection()

    }
}