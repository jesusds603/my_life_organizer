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

    var confirmDelete by remember { mutableStateOf(false) }

    val occurrencesForHabit = occurrences.filter { occurrence ->
        occurrence.habitId == habit.habitId
    }


    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { changeShowMenu(false) }, // Cierra el menú al hacer clic afuera
        modifier = Modifier.background(themeColors.backGround1)
    ) {
        DropdownMenuItem(
            text = { Text(
                text= "Delete",
                color = themeColors.text1
            ) },
            onClick = {
                changeShowMenu(false)
                confirmDelete = true
            },
            modifier = Modifier.background(themeColors.backGround3)
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
                        text = "Aceptar",
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
                        text = "Cancelar",
                        color = themeColors.text1
                    )
                }
            },
            title = {
                Text(
                    text = "Delete Habit",
                    color = themeColors.text1
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this habit?",
                    color = themeColors.text1,
                    fontSize = 14.sp
                )
            },
            containerColor = themeColors.backGround2
        )
    }
}