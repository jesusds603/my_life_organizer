package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectColors() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val colorNewHabit = appViewModel.colorNewHabit.value
    val namesColor = themeViewModel.namesColorCategories

    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Color",
            color = themeColors.text1,
        )

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    color = themeViewModel.getCategoryColor(colorNewHabit)
                ),
        )
    }
    LazyRow {
        items(namesColor) { color ->
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(
                        color = themeViewModel.getCategoryColor(color)
                    )
                    .clickable {
                        appViewModel.changeColorNewHabit(color)
                    }
            )
        }
    }
}