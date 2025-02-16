package com.example.mylifeorganizer.components.habits.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.HabitEntity
import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatingOptionsHabit(
    showMenu: Boolean = false,
    changeShowMenu: (Boolean) -> Unit,
    habit: HabitEntity,
    occurrences: List<HabitOccurrenceEntity>
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    var confirmDelete by remember { mutableStateOf(false) }

    val occurrencesForHabit = occurrences.filter { occurrence ->
        occurrence.habitId == habit.habitId
    }


    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { changeShowMenu(false) }, // Cierra el menú al hacer clic afuera
        modifier = Modifier.background(themeColors.bgDialog)
    ) {
        DropdownMenuItem(
            text = { Text(
                text= if(isLangEng) "Delete" else "Borrar",
                color = themeColors.text1
            ) },
            onClick = {
                changeShowMenu(false)
                confirmDelete = true
            },
            modifier = Modifier.background(themeColors.backGround1)
        )
    }

    if(confirmDelete) {
        AlertDialog(
            onDismissRequest = {
                confirmDelete = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        occurrencesForHabit.forEach { occurrence ->
                            Log.d("HabitDeletion", "Deleting occurrence: ${occurrence.date}")
                            noteViewModel.deleteHabitOccurrence(occurrence)
                        }
                        Log.d("HabitDeletion", "Habit and occurrences deleted successfully")

                        noteViewModel.deleteHabit(
                            habit
                        )

                        confirmDelete = false
                    }
                ) {
                    Text(
                        text = if(isLangEng) "Delete" else "Borrar",
                        color = themeColors.text1
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        confirmDelete = false
                    }
                ) {
                    Text(
                        text = if(isLangEng) "Cancel" else "Cancelar",
                        color = themeColors.text1
                    )
                }
            },
            title = {
                Text(
                    text = if(isLangEng) "Delete Habit" else "Borrar Hábito",
                    color = themeColors.text1
                )
            },
            text = {
                Text(
                    text = if(isLangEng) "Are you sure you want to delete this habit?" else "Estas seguro que quieres borrar este hábito?",
                    color = themeColors.text1,
                    fontSize = 14.sp
                )
            },
            containerColor = themeColors.bgDialog
        )
    }
}