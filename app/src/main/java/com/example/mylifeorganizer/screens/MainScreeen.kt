package com.example.mylifeorganizer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.ButtonTabs
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.example.mylifeorganizer.components.addnewnote.AddNoteWindow
import com.example.mylifeorganizer.components.notes.noteview.NoteWindow

@Composable
fun MainScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isAddingNote = appViewModel.isAddingNote.value
    val isShowingNote = appViewModel.isShowingNote.value


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColors.backGround1)
            .systemBarsPadding()
    ) {
        // Mostrar la pantalla correspondiente
        if (isAddingNote) {

            // Mostrar AddNoteWindow ocupando toda la pantalla
            AddNoteWindow(modifier = Modifier.fillMaxSize())
        }  else if (isShowingNote) {
            NoteWindow()
        } else {
            // Mostrar ContentMainScreen cuando no se está agregando una nota
            ContentMainScreen()
        }
    }
}

@Composable
fun ContentMainScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val selectedTab = appViewModel.selectedTab.value
    val themeColors = themeViewModel.themeColors.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColors.backGround2)
    ) {
        // Parte superior: mostrar la pantalla seleccionada
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                "Home" -> HomeScreen()
                "Notes" -> NotesScreen()
                "Tasks" -> TasksScreen()
                "Habits" -> HabitsScreen()
                "Calendar" -> CalendarScreen()
                "Dashboard" -> DashboardScreen()
            }
        }

        // Parte inferior: los botones de navegación
        ButtonTabs()
    }
}
