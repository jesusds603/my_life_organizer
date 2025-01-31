package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowCategories() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel

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

