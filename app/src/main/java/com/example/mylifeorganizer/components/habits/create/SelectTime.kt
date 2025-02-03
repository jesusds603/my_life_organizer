package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectTime() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value

    val timeForNewHabit = appViewModel.timeForNewHabit.value
    val timeOptions = listOf("any", "morning", "afternoon", "night", "custom")

    // Opciones de hora con sus representaciones más amigables
    val timeLabels = mapOf(
        "any" to "Anytime",
        "morning" to "Morning",
        "afternoon" to "Afternoon",
        "night" to "Night",
        "custom" to "Set Time"
    )


    // Variables para hora y minuto
    var selectedHour by remember { mutableStateOf("08") }
    var selectedMinute by remember { mutableStateOf("00") }
    var showTimeSelector by remember { mutableStateOf(false) }
    var confirmedTime by remember { mutableStateOf(false) }


    Column {
        Row {
            // Tiempo
            Text(
                text = "Do it at:",
                color = themeColors.text1,
            )

            // Si el tiempo está confirmado, mostrar la hora seleccionada
            if (confirmedTime) {
                Text(
                    text = "$selectedHour:$selectedMinute",
                    color = themeColors.text1,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }

        LazyRow (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            items(timeOptions) { time ->
                Button(
                    onClick = {
                        appViewModel.changeTimeForNewHabit(time)
                        if (time == "custom") {
                            showTimeSelector = true
                        } else {
                            showTimeSelector = false
                            confirmedTime = false // Resetear la confirmación
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (timeForNewHabit == time || (time == "custom" && confirmedTime)) Color.Magenta else themeColors.backGround1
                    )
                ) {
                    Text(
                        text = timeLabels[time] ?: time.replaceFirstChar { it.uppercase() },
                        color = themeColors.text1,
                    )
                }
            }
        }

        // Si se selecciona "hh:mm", mostrar un selector de hora y minuto
        if (showTimeSelector) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                // Selector de horas
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Fila para las horas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "HH: ",
                            color = themeColors.text1,
                            modifier = Modifier
                                .width(40.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp) // Para agregar algo de espacio antes del "HH"
                        )
                        LazyRow(
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp)
                                .background(themeColors.backGround1),
                        ) {
                            items((0..23).toList()) { hour ->
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            selectedHour = hour.toString().padStart(2, '0')
                                        }
                                        .padding(1.dp)
                                        .background(color = themeColors.backGround2)
                                ) {
                                    Text(
                                        text = hour.toString().padStart(2, '0'),
                                        color = if (selectedHour == hour.toString().padStart(2, '0')) Color.Magenta
                                        else themeColors.text1,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }

                    // Fila para los minutos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "MM: ",
                            color = themeColors.text1,
                            modifier = Modifier
                                .width(40.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp) // Para agregar algo de espacio antes del "MM"
                        )
                        LazyRow(
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp)
                                .background(themeColors.backGround1),
                        ) {
                            items((0..59).toList()) { minute ->
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            selectedMinute = minute.toString().padStart(2, '0')
                                        }
                                        .padding(1.dp)
                                        .background(color = themeColors.backGround2)
                                ) {
                                    Text(
                                        text = minute.toString().padStart(2, '0'),
                                        color = if (selectedMinute == minute.toString().padStart(2, '0')) Color.Magenta else themeColors.text1,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }


                // Mostrar la hora seleccionada
                Text(
                    text = "$selectedHour:$selectedMinute",
                    color = themeColors.text1,
                    modifier = Modifier.padding(top = 10.dp)
                )

                // Botón para confirmar la selección de hora
                Button(
                    onClick = {
                        // Actualizar el tiempo seleccionado
                        appViewModel.changeTimeForNewHabit("$selectedHour:$selectedMinute")
                        showTimeSelector = false // Ocultar el selector
                        confirmedTime = true // Confirmar el tiempo seleccionado
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Magenta
                    )
                ) {
                    Text(text = "OK", color = themeColors.text1)
                }
            }
        }

    }
}