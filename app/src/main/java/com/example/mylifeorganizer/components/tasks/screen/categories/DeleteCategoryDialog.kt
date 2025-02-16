package com.example.mylifeorganizer.components.tasks.screen.categories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteCategoryDialog(
    name: String,
    changeShowDeleteDialog: (Boolean) -> Unit,
    category: CategoryTaskEntity?,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    AlertDialog(
        onDismissRequest = { changeShowDeleteDialog(false) },
        confirmButton = {
            Button(
                onClick = {
                    if(category != null) {
                        noteViewModel.deleteRelationTaskCategory(category.categoryId)
                        noteViewModel.deleteCategoryTasks(category)
                    }
                    changeShowDeleteDialog(false)
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
                    changeShowDeleteDialog(false)
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
                text = if(isLangEng) "Delete Category" else "Borrar Categoría",
                color = themeColors.text1
            )
        },
        text = {
            Text(
                text = if(isLangEng) "Are you sure you want to delete the category '$name?" else "Está seguro que desea borrar la categoría '$name'?",
                color = themeColors.text1
            )
        },
        containerColor = themeColors.bgDialog
    )
}