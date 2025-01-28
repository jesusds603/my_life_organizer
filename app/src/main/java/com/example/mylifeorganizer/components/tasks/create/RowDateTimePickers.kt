package com.example.mylifeorganizer.components.tasks.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun RowDateTimePickers(
    onShowDatePicker: (Boolean) -> Unit,
    onShowTimePicker: (Boolean) -> Unit,
    dueDate: Long,
    dueTime: Long,
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        // Botón para seleccionar la fecha
        Button(
            onClick = {
                onShowDatePicker(true)
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = themeColors.backGround2
            )
        ) {
            Text(
                text = if (dueDate > 0) formatDate(dueDate) else "Due Date",
                color = themeColors.text1
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para seleccionar la hora
        Button(
            onClick = {
                onShowTimePicker(true)
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = themeColors.backGround2
            )
        ) {
            Text(
                text = if (dueTime > 0) formatTime(dueTime) else "Due Time",
                color = themeColors.text1
            )
        }
    }
}