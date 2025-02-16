package com.example.mylifeorganizer.components.tasks.screen.categories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryBox(
    category: CategoryTaskEntity?,
    name: String,
    bgColor: String,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var showOptions by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(
                color = if(name == "All") {
                    themeColors.backGround1
                } else {
                    themeViewModel.getCategoryColor(bgColor)
                }
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable {
                appViewModel.updateNameSelectedCategorieTasksScreen(name)
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showOptions = true
                    }
                )
            }
    ) {
        Text(
            text = name,
            color = themeColors.text1
        )
    }

    if(showOptions) {
        OptionsCategoryTask(
            showOptions = showOptions,
            changeShowOptions = { showOptions = it },
            changeShowEditDialog = { showEditDialog = it },
            changeShowDeleteDialog = { showDeleteDialog = it }
        )
    }

    if(showEditDialog) {
        EditCategoryDialog(
            name = name,
            bgColor = bgColor,
            category = category,
            changeShowEditDialog = { showEditDialog = it }
        )
    }

    if(showDeleteDialog) {
        DeleteCategoryDialog(
            name = name,
            changeShowDeleteDialog = { showDeleteDialog = it },
            category = category
        )
    }
}