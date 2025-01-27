package com.example.mylifeorganizer.components.tasks.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun CategoryDialogTask (
    noteViewModel: NoteViewModel,
    onShowCreateCategoryDialog: (Boolean) -> Unit,
    newCategoryName: String,
    onNewCategoryName: (String) -> Unit,
    newCategoryColor: String,
    onNewCategoryColor: (String) -> Unit,
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val namesColors = themeViewModel.namesColorCategories


    AlertDialog(
        onDismissRequest = {
            onShowCreateCategoryDialog(false)
            onNewCategoryName("")
            onNewCategoryColor("")
        },
        confirmButton = {
            Button(
                onClick = {
                    onShowCreateCategoryDialog(false)
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
        },
        dismissButton = {
            Button(
                onClick = { onShowCreateCategoryDialog(false) }
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
            }
        },
        containerColor = themeColors.backGround2
    )
}