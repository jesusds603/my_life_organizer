package com.example.mylifeorganizer.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.home.HabitsSection
import com.example.mylifeorganizer.components.home.Header
import com.example.mylifeorganizer.components.home.QuickCard
import com.example.mylifeorganizer.components.home.TasksSection
import com.example.mylifeorganizer.components.home.getQuickCardItems
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
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
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
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
                                .height(40.dp) // Ajusta la altura
                                .aspectRatio(1f)
                        )
                    },
                    onClick = item.onClick,
                    backgroundColor = item.backgroundColor
                )
            }
        }

        Row (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            TasksSection(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = Color.Magenta,
                            start = Offset(size.width - strokeWidth, 0f),
                            end = Offset(size.width, size.height - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(2.dp),
            )


            HabitsSection (
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = Color.Magenta,
                            start = Offset(size.width - strokeWidth, 0f),
                            end = Offset(size.width, size.height - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(2.dp),
            )
        }
    }


}
