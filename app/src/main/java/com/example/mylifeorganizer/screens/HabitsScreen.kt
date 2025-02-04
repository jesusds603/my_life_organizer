package com.example.mylifeorganizer.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.habits.create.MainDialog
import com.example.mylifeorganizer.components.habits.screen.HabitsContent
import com.example.mylifeorganizer.components.habits.screen.Header
import com.example.mylifeorganizer.components.habits.screen.RowDays
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitsScreen() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val habits = noteViewModel.habits.collectAsState(initial = emptyList()).value
    val habitsOccurrences = noteViewModel.habitsOccurrences.collectAsState(initial = emptyList()).value

    val isAddingHabit = appViewModel.isAddingHabit.value

    // Estado para el día seleccionado
    var selectedDate by remember { mutableStateOf(
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(
        Calendar.getInstance().time)) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header()

        RowDays(
            habitsOccurrences = habitsOccurrences,
            onDateSelected = { date -> selectedDate = date }
        )

        HabitsContent(
            selectedDate = selectedDate,
            habits = habits,
            habitsOccurrences = habitsOccurrences
        )

        if (isAddingHabit) {
            MainDialog()
        }
    }
}