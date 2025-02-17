package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditCategoryDialog(
    changeShowEditDialog: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    AlertDialog(
        onDismissRequest = {
            changeShowEditDialog(false)
        },
        confirmButton = {
            Button(
                onClick = {
                    changeShowEditDialog(false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = if(isLangEng) "Save" else "Guardar",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    changeShowEditDialog(false)
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
                text = if(isLangEng) "Edit Category" else "Editar Categoría",
                color = themeColors.text1
            )
        },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {  }
        },
        containerColor = themeColors.bgDialog,
    )
}