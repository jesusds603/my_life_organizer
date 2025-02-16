package com.example.mylifeorganizer.components.tasks.create.datetime

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowDateTimePickers(
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val selectedDueDate = appViewModel.selectedDueDate
    val selectedDueTime = appViewModel.selectedDueTime


    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        // Botón para seleccionar la fecha
        Column (
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if(isLangEng) "Due Date" else "Fecha límite",
                color = themeColors.text1
            )
            Button(
                onClick = {
                    appViewModel.toggleShowDatePicker()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(
                    text = if (selectedDueDate == "") "----/--/--" else selectedDueDate,
                    color = themeColors.text1
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para seleccionar la hora
        Column (
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if(isLangEng) "Time (Optional)" else "Hora (Opcional)",
                    color = themeColors.text1
                )

                Box(
                    modifier = Modifier
                        .clickable {
                            appViewModel.updateSelectedDueTime("")
                        }
                        .background(
                            color = themeColors.buttonDelete
                        )
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "x",
                        color = themeColors.text1
                    )
                }
            }
            Button(
                onClick = {
                    appViewModel.toggleShowTimePicker()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(
                    text = if (selectedDueTime != "") selectedDueTime else "--:--",
                    color = themeColors.text1
                )
            }
        }
    }
}