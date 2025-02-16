package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogDeleteTask(
    changeShowDialogDeleteTask: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val taskIdSelectedScreen = appViewModel.taskIdSelectedScreen
    val nameSelectedCategoryTasksScreen = appViewModel.nameSelectedCategorieTasksScreen


    AlertDialog(
        onDismissRequest = { changeShowDialogDeleteTask(false) },
        confirmButton = {
            Button(
                onClick = {
                    if(taskIdSelectedScreen != null) {
                        // Borrar las relaciones
                        noteViewModel.deleteTaskCategories(taskId = taskIdSelectedScreen)
                        // Borrar tarea y ocurrencias
                        noteViewModel.deleteTaskWithOccurrences(taskId = taskIdSelectedScreen)
                    }
                    changeShowDialogDeleteTask(false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )

            ) {
                Text(
                    text = if(isLangEng) "Delete" else "Borrar",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    changeShowDialogDeleteTask(false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = if(isLangEng) "Cancel" else "Cancelar",
                    color = themeColors.text1
                )
            }
        },
        title = {
            Text(
                text = if(isLangEng) "Delete Task" else "Borrar Tarea",
                color = themeColors.text1
            )
        },
        text = {
            Text(
                text = "${if (isLangEng) "Are you sure you want to delete the task" else "Está seguro que desea borrar la tarea"} '$nameSelectedCategoryTasksScreen'?",
                color = themeColors.text1
            )
        },
        containerColor = themeColors.bgDialog
    )
}