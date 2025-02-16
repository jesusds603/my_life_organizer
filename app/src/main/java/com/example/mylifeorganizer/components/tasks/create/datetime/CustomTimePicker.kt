package com.example.mylifeorganizer.components.tasks.create.datetime

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
    val isLangEng = appViewModel.isLangEng.value

    // Convertir selectedDueTime a LocalTime o usar la hora actual si está vacío
    val initialTime = if (appViewModel.selectedDueTime.isNotEmpty()) {
        LocalTime.parse(appViewModel.selectedDueTime, DateTimeFormatter.ofPattern("HH:mm"))
    } else {
        LocalTime.now()
    }

    // Estado interno para manejar la hora y los minutos seleccionados
    var selectedHour by remember { mutableStateOf(initialTime.hour) }
    var selectedMinute by remember { mutableStateOf(initialTime.minute) }

    // Estado para alternar entre la selección de horas y minutos
    var isHourSelected by remember { mutableStateOf(false) }

    // Normalizar valores (si es 24 -> 00 para hora; si es 60 -> 00 para minutos)
    val normalizedHour = if (selectedHour == 24) 0 else selectedHour
    val normalizedMinute = if (selectedMinute == 60) 0 else selectedMinute

    AlertDialog(
        onDismissRequest = { appViewModel.toggleShowTimePicker() },
        title = {
            Text(
                text = "${if (isLangEng) "Selected Time" else "Hora seleccionada"}: ${"%02d".format(normalizedHour)}:${"%02d".format(normalizedMinute)}",
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
                    // Círculo de minutos
                    MinuteDial(
                        selectedMinute = selectedMinute,
                        onMinuteSelected = { selectedMinute = it }
                    )
                } else {
                    // Círculo de horas (dos anillos para 1-12 y 13-24)
                    HourDial(
                        selectedHour = selectedHour,
                        onHourSelected = { selectedHour = it }
                    )
                }

            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isHourSelected) {
                        // Actualizar `selectedDueTime` en el ViewModel con el valor seleccionado
                        val formattedTime = "${"%02d".format(normalizedHour)}:${"%02d".format(normalizedMinute)}"
                        appViewModel.updateSelectedDueTime(formattedTime)
                        appViewModel.toggleShowTimePicker()
                    } else {
                        isHourSelected = true
                    }
                }
            ) {
                Text(
                    text = if(isLangEng) "Next" else "Siguiente",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { appViewModel.toggleShowTimePicker() }) {
                Text(
                    text = if(isLangEng) "Cancel" else "Cancelar",
                    color = themeColors.text1
                )
            }
        },
        containerColor = themeColors.backGround1
    )
}