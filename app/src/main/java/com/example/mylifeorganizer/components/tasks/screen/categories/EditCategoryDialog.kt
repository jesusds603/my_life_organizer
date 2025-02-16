package com.example.mylifeorganizer.components.tasks.screen.categories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditCategoryDialog(
    name: String,
    bgColor: String,
    category: CategoryTaskEntity?,
    changeShowEditDialog: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val namesColors = themeViewModel.namesColorCategories
    var newName by remember { mutableStateOf(name) }
    var newColor by remember { mutableStateOf(bgColor) }

    AlertDialog(
        onDismissRequest = { changeShowEditDialog(false) },
        confirmButton = {
            Button(
                onClick = {
                    noteViewModel.updateCategoryTask(
                        category!!.copy(
                            name = newName,
                            bgColor = newColor
                        )
                    )
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
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = {
                        Text(
                            text = if(isLangEng) "New Name" else "Nuevo Nombre",
                            color = themeColors.text1
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = themeColors.text1,
                        unfocusedTextColor = themeColors.text2,
                        focusedContainerColor = themeViewModel.getCategoryColor(newColor),
                        unfocusedContainerColor = themeViewModel.getCategoryColor(newColor),
                    )
                )

                Spacer(modifier = Modifier.padding(8.dp))

                FlowRow (
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    namesColors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(4.dp)
                                .background(themeViewModel.getCategoryColor(color))
                                .clickable { newColor = color },
                        )
                    }
                }
            }
        },
        containerColor = themeColors.bgDialog
    )
}