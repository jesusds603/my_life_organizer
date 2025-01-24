package com.example.mylifeorganizer.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.home.Header
import com.example.mylifeorganizer.components.home.QuickCard
import com.example.mylifeorganizer.components.home.getQuickCardItems
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun HomeScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value
    var isThemeDark = themeViewModel.isThemeDark.value
    var isLangEng = appViewModel.isLangEng.value
    var isAddingNote = appViewModel.isAddingNote.value

    val quickCardItems = getQuickCardItems(themeColors, appViewModel)

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Header()


        // Cards
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(quickCardItems) { item ->
                QuickCard(
                    title = item.title,
                    subtitle = item.subtitle,
                    icon = {
                        Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = item.title,
                            modifier = Modifier
                                .height(50.dp) // Ajusta la altura
                                .aspectRatio(1f)
                        )
                    },
                    onClick = item.onClick,
                    backgroundColor = item.backgroundColor
                )
            }
        }
    }


    // DeleteDB()
}
