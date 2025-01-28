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
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomTimePicker() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Convertir selectedDueTime a LocalTime o usar la hora actual si está vacío
    val initialTime = if (appViewModel.selectedDueTime.isNotEmpty()) {
        LocalTime.parse(appViewModel.selectedDueTime, DateTimeFormatter.ofPattern("HH:mm"))
    } else {
        LocalTime.now()
    }

    // Estado interno para manejar la hora y los minutos seleccionados
    var selectedHour by remember { mutableStateOf(initialTime.hour) }

    // Estados para las horas y minutos seleccionados
    var selectedHour12 by remember { mutableStateOf<Int?>(null) } // Horas del círculo interno
    var selectedHour24 by remember { mutableStateOf<Int?>(null) } // Horas del círculo externo
    var selectedMinute by remember { mutableStateOf(initialTime.minute) }

    // Estado para alternar entre la selección de horas y minutos
    var isHourSelected by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { appViewModel.toggleShowTimePicker() },
        title = {
            val formattedHour = selectedHour24 ?: selectedHour12 ?: selectedHour
            Text(
                text = "Selected Time: ${"%02d".format(formattedHour)}:${"%02d".format(selectedMinute)}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = themeColors.text1
            )
        },
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
            ) {
                if (isHourSelected) {
                    // Círculo de minutos (solo múltiplos de 5)
                    CircleDial(
                        items = (0..59).toList(),
                        selectedValue = selectedMinute,
                        onValueSelected = { selectedMinute = it },
                        isActive = true,
                        radiusFraction = 1f,
                        isMinuteDial = true,
                        showDotsForMissing = true
                    )
                } else {
                    // Círculo de horas (dos anillos)
                    // Círculo de horas (dos anillos para 1-12 y 13-24)
                    CircleDial(
                        items = (1..12).toList(),
                        selectedValue = selectedHour12 ?: selectedHour,
                        onValueSelected = {
                            selectedHour12 = it
                            selectedHour24 = null // Asegurar que no se use la hora del círculo externo
                        },
                        isActive = true,
                        radiusFraction = 0.5f,
                        isMinuteDial = false,
                        isOuterCircle = false
                    )
                    CircleDial(
                        items = (13..24).toList(),
                        selectedValue = selectedHour24 ?: selectedHour,
                        onValueSelected = {
                            selectedHour24 = it
                            selectedHour12 = null // Asegurar que no se use la hora del círculo interno
                        },
                        isActive = true,
                        radiusFraction = 1f,
                        isMinuteDial = false,
                        isOuterCircle = true
                    )
                }

            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isHourSelected) {
                        // Actualizar `selectedDueTime` en el ViewModel con el valor seleccionado
                        val finalHour = selectedHour24 ?: selectedHour12 ?: selectedHour
                        val formattedTime = "${"%02d".format(finalHour)}:${"%02d".format(selectedMinute)}"
                        appViewModel.updateSelectedDueTime(formattedTime)
                        appViewModel.toggleShowTimePicker()
                    } else {
                        isHourSelected = true
                    }
                }
            ) {
                Text("Next", color = themeColors.text1)
            }
        },
        dismissButton = {
            TextButton(onClick = { appViewModel.toggleShowTimePicker() }) {
                Text("Cancel", color = themeColors.text1)
            }
        },
        containerColor = themeColors.backGround1
    )
}