package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainDialog() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleAddingHabit()
        },
        confirmButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingHabit()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround3
                )
            ) {
                Text(
                    text = "Aceptar",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingHabit()

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround3
                )
            ) {
                Text(
                    text = "Cancelar",
                    color = themeColors.text1
                )
            }
        },
        title = {
            Text(
                text = "Create Habit",
                color = themeColors.text1
            )
        },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {

            }
        },
        containerColor = themeColors.backGround2
    )
}