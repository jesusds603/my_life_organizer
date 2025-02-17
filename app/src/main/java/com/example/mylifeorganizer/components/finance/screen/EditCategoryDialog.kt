package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditCategoryDialog(
    category: CategoryFinanceEntity,
    changeShowEditDialog: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val namesColorCategories = themeViewModel.namesColorCategories

    var newName by remember { mutableStateOf(category.name) }
    var newColor by remember { mutableStateOf(category.bgColor) }

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
            ) {
                Text(
                    text = if(isLangEng) "Name" else "Nombre",
                    color = themeColors.text1
                )
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = {
                        Text(
                            text = if (isLangEng) "Name" else "Nombre",
                            color = themeColors.text1
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        cursorColor = themeColors.text1,
                        focusedTextColor = themeColors.text1,
                        unfocusedTextColor = themeColors.text1,
                        focusedContainerColor = themeViewModel.getCategoryColor(newColor),
                        unfocusedContainerColor = themeViewModel.getCategoryColor(newColor),
                    )
                )

                Text(
                    text = if(isLangEng) "Color" else "Color",
                    color = themeColors.text1
                )
                FlowRow {
                    namesColorCategories.forEach { colorName ->
                        Button(
                            onClick = {
                                newColor = colorName
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = themeViewModel.getCategoryColor(colorName)
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {}
                    }
                }
            }
        },
        containerColor = themeColors.bgDialog,
    )
}