package com.example.mylifeorganizer.components.tasks.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomTimePicker(
) {
    val appViewModel: AppViewModel = viewModel()
    var selectedHour = appViewModel.selectedDueTime.first // primer elemento de la pareja
    var selectedMinute = appViewModel.selectedDueTime.second
    var isHourSelected by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = {appViewModel.toggleShowTimePicker()},
        title = {
            Text(
                text = "Selected Time: ${"%02d".format(selectedHour)}:${"%02d".format(selectedMinute)}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
            ) {
                // Outer circle for minutes
                CircleDial(
                    items = (0..59).toList(),
                    selectedValue = selectedMinute,
                    onValueSelected = { selectedMinute = it },
                    isActive = !isHourSelected
                )

                // Inner circle for hours
                CircleDial(
                    items = (0..23).toList(),
                    selectedValue = selectedHour,
                    onValueSelected = { selectedHour = it },
                    isActive = isHourSelected,
                    radiusFraction = 0.5f
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                appViewModel.updateSelectedDueTime(selectedHour, selectedMinute)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = {appViewModel.toggleShowTimePicker()}) {
                Text("Cancel")
            }
        }
    )
}