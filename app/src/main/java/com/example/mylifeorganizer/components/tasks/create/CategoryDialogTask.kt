package com.example.mylifeorganizer.components.tasks.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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

    val availableCategories by noteViewModel.categoriesTasks.collectAsState(emptyList())
    val selectedCategoriesTask = appViewModel.selectedCategoriesTask


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
                }
            ) {
                Text(text = "OK", color = themeColors.text1)
            }
        },
        dismissButton = {
            Button(
                onClick = { appViewModel.toggleShowCreateCategoryDialogTask() }
            ) {
                Text(text = "Cancel", color = themeColors.text1)
            }
        },
        title = { Text(
            text = "Add New Category",
            color = themeColors.text1
        ) },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Available categories:", color = themeColors.text1, modifier = Modifier.padding(bottom = 8.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // Espacio entre la LazyRow y el Box

                ) {
                    items(availableCategories) { category ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(themeViewModel.getCategoryColor(category.bgColor))
                                .clickable {
                                    appViewModel.updateSelectedCategoriesTask( selectedCategoriesTask + category )
                                }
                        ) {
                            Text(text = category.name, color = themeColors.text1)
                        }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Selected categories:", color = themeColors.text1)

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // Espacio entre la LazyRow y el Box

                ) {
                    items(selectedCategoriesTask) { category ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(themeViewModel.getCategoryColor(category.bgColor))
                                .clickable {
                                    appViewModel.updateSelectedCategoriesTask( selectedCategoriesTask - category )
                                }
                        ) {
                            Text(text = category.name, color = themeColors.text1)
                        }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Create new category:", color = themeColors.text1, modifier = Modifier.padding(bottom = 8.dp))
                TextField(
                    value = newCategoryName,
                    onValueChange = { onNewCategoryName(it) },
                    label = { Text("Category Name", color = themeColors.text1) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = themeColors.text1,
                        unfocusedTextColor = themeColors.text2,
                        focusedContainerColor = themeViewModel.getCategoryColor(newCategoryColor),
                        unfocusedContainerColor = themeViewModel.getCategoryColor(newCategoryColor),
                        cursorColor = Color.Magenta
                    )
                )

                LazyRow {
                    items(namesColors) { categoryColor ->
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(themeViewModel.getCategoryColor(categoryColor))
                                .clickable { onNewCategoryColor(categoryColor) }
                        )
                    }
                }

                TextButton(
                    onClick = {
                        noteViewModel.addCategoryTask(
                            CategoryTaskEntity(
                                name = newCategoryName,
                                bgColor = newCategoryColor
                            )
                        )
                        onNewCategoryName("")
                        onNewCategoryColor("")
                    }
                ) {
                    Text(text = "Create Category", color = themeColors.text1)
                }
            }
        },
        containerColor = themeColors.backGround2
    )
}