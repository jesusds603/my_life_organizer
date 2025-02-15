package com.example.mylifeorganizer.components.notes.common.categories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogEditCategory(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    textFieldValue: String,
    textFieldOnValueChange: (String) -> Unit,
    bgCategory: String,
    onBgCategoryChange: (String) -> Unit
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
                    color = MaterialTheme.colorScheme.onSecondary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                content = {
                    Text(
                        text = if(isLangEng) "Rename category" else "Renombrar categoría",
                        color = themeColors.text1
                    )

                    TextField(
                        value = textFieldValue,
                        onValueChange = { textFieldOnValueChange(it) },
                        label = {
                            Text(
                                text = if(isLangEng) "New name" else "Nuevo nombre",
                                color = themeColors.text1
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = themeColors.text1,
                            unfocusedTextColor = themeColors.text2,
                            focusedContainerColor = themeViewModel.getCategoryColor(bgCategory),
                            unfocusedContainerColor = themeViewModel.getCategoryColor(bgCategory),
                        )
                    )

                    Text(
                        text = if(isLangEng) "Change color" else "Cambiar color",
                        color = themeColors.text1
                    )

                    FlowRow (
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        themeViewModel.namesColorCategories.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(4.dp)
                                    .background(
                                        color = themeViewModel.getCategoryColor(color),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .clickable {
                                        onBgCategoryChange(color)
                                    }
                            )
                        }
                    }
                }
            )
        },
        title = {
            Text(
                text = if(isLangEng) "Edit category" else "Editar categoría",
                style = MaterialTheme.typography.titleLarge,
                color = themeColors.text1
            )
        },
        containerColor = themeColors.bgDialog,
    )
}
