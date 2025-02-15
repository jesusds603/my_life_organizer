package com.example.mylifeorganizer.components.notes.common.categories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogDeleteCategory(
    category: CategoryEntity,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = if(isLangEng) "Confirm" else "Confirmar",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
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
        text = {
            Text(
                text = if(isLangEng) "Are you sure you want to delete the category '${category.name}'?" else "Está seguro que desea eliminar esta categoría '${category.name}'?",
                color = themeColors.text1
            )
        },
        title = {
            Text(
                text = if(isLangEng) "Delete category" else "Eliminar categoría",
                color = themeColors.text1
            )
        },
        containerColor = themeColors.bgDialog,
    )
}