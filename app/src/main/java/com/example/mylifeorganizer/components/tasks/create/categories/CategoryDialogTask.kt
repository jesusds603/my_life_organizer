package com.example.mylifeorganizer.components.tasks.create.categories

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryDialogTask (
    newCategoryName: String,
    onNewCategoryName: (String) -> Unit,
    newCategoryColor: String,
    onNewCategoryColor: (String) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val namesColors = themeViewModel.namesColorCategories
    val isLangEng = appViewModel.isLangEng.value

    val availableCategories by noteViewModel.categoriesTasks.collectAsState(emptyList())
    val selectedCategoriesTask = appViewModel.selectedCategoriesTask

    var showCreateCategory by remember { mutableStateOf(false) }


    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleShowCreateCategoryDialogTask()
            onNewCategoryName("")
            onNewCategoryColor("")
        },
        confirmButton = {
            Button(
                onClick = {
                    appViewModel.toggleShowCreateCategoryDialogTask()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1,
                    contentColor = themeColors.text1
                )
            ) {
                Text(
                    text = "OK",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    appViewModel.toggleShowCreateCategoryDialogTask()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1,
                    contentColor = themeColors.text1
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
                text = if(isLangEng) "Add new category" else "Añadir nueva categoría",
                color = themeColors.text1
            )
        },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = if (isLangEng) "Selected categories:" else "Categorías seleccionadas:",
                    color = themeColors.text1
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(selectedCategoriesTask) { category ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(themeViewModel.getCategoryColor(category.bgColor))
                                .clickable {
                                    appViewModel.updateSelectedCategoriesTask(selectedCategoriesTask - category)
                                }
                        ) {
                            Text(text = category.name, color = themeColors.text1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Available categories:",
                    color = themeColors.text1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    items(availableCategories) { category ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(themeViewModel.getCategoryColor(category.bgColor))
                                .clickable {
                                    appViewModel.updateSelectedCategoriesTask(selectedCategoriesTask + category)
                                }
                        ) {
                            Text(text = category.name, color = themeColors.text1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showCreateCategory = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColors.backGround1,
                        contentColor = themeColors.text1
                    )
                ) {
                    Text(
                        text = "+",
                        color = themeColors.text1,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                if (showCreateCategory) {
                    TextField(
                        value = newCategoryName,
                        onValueChange = { onNewCategoryName(it) },
                        label = {
                            Text(
                                text = if(isLangEng) "Name" else "Nombre",
                                color = themeColors.text1
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = themeColors.text1,
                            unfocusedTextColor = themeColors.text2,
                            focusedContainerColor = themeViewModel.getCategoryColor(newCategoryColor),
                            unfocusedContainerColor = themeViewModel.getCategoryColor(newCategoryColor),
                            cursorColor = Color.Magenta
                        )
                    )

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
                                    .clickable { onNewCategoryColor(color) },
                            )
                        }
                    }


                    Button(
                        onClick = {
                            noteViewModel.addCategoryTask(
                                CategoryTaskEntity(
                                    name = newCategoryName,
                                    bgColor = newCategoryColor
                                )
                            )
                            onNewCategoryName("")
                            onNewCategoryColor("")
                            showCreateCategory = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColors.backGround1,
                            contentColor = themeColors.text1
                        ),
                    ) {
                        Text(
                            text = if(isLangEng) "Create" else "Crear",
                            color = themeColors.text1
                        )
                    }
                }

            }
        },
        containerColor = themeColors.bgDialog
    )
}