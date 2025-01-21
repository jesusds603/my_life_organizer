package com.example.mylifeorganizer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.ButtonTabs
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    var selectedTab = appViewModel.selectedTab.value
    var themeColors = themeViewModel.themeColors.value
    var isThemeDark = themeViewModel.isThemeDark.value

    // Controlador del sistema
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = themeColors.backGround1,
        darkIcons = !isThemeDark
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColors.backGround2)
    ) {
        // Parte superior
        Box(modifier = Modifier.weight(1f)) {
            when(selectedTab) {
                "Home" -> HomeScreen()
                "Notes" -> NotesScreen()
                "Tasks" -> TasksScreen()
                "Habits" -> HabitsScreen()
                "Calendar" -> CalendarScreen()
                "Dashboard" -> DashboardScreen()
            }
        }

        // Parte inferior Botones
        ButtonTabs()
    }
}