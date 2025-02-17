package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowCategoriesFinance() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val categories = noteViewModel.categoriesFinance.collectAsState(initial = emptyList()).value

    LazyRow (
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .background(color = themeColors.backGround1)
            .padding(horizontal = 2.dp, vertical = 1.dp)
    ) {
        item {
            CategoryBoxFinance(
                category = null,
                name = "All",
                bgColor = ""
            )
        }

        items(categories) { category ->
            CategoryBoxFinance(
                category = category,
                name = category.name,
                bgColor = category.bgColor
            )
        }
    }
}