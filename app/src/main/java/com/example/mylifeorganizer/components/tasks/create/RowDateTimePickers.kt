package com.example.mylifeorganizer.components.tasks.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    val selectedDueDate = appViewModel.selectedDueDate
    val selectedDueTime = appViewModel.selectedDueTime


    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        // Botón para seleccionar la fecha
        Button(
            onClick = {
                appViewModel.toggleShowDatePicker()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = themeColors.backGround2
            )
        ) {
            Text(
                text = if (selectedDueDate == "") "Due Date" else selectedDueDate,
                color = themeColors.text1
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para seleccionar la hora
        Button(
            onClick = {
                appViewModel.toggleShowTimePicker()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = themeColors.backGround2
            )
        ) {
            Text(
                text = if (selectedDueTime != "") selectedDueTime else "Due Time",
                color = themeColors.text1
            )
        }
    }
}