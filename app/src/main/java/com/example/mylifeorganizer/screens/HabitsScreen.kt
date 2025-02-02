package com.example.mylifeorganizer.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.habits.create.MainDialog
import com.example.mylifeorganizer.components.habits.screen.Header
import com.example.mylifeorganizer.components.habits.screen.RowDays
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitsScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val isAddingHabit = appViewModel.isAddingHabit.value

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Header()

        RowDays()

        if(isAddingHabit) {
            MainDialog()
        }
    }
}