package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
fun RowCategories() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val categoriesTasks = noteViewModel.categoriesTasks.collectAsState(initial = emptyList()).value

    LazyRow (
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            CategoryBox(
                name = "All",
                bgColor = ""
            )
        }

        items(categoriesTasks) { category ->
            CategoryBox(
                name = category.name,
                bgColor = category.bgColor
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryBox(
    name: String,
    bgColor: String,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

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
    ) {
        Text(
            text = name,
            color = themeColors.text1
        )
    }
}