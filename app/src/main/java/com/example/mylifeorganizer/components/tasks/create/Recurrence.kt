package com.example.mylifeorganizer.components.tasks.create

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Recurrence() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val isTaskRecurring = appViewModel.isTaskRecurring
    val recurrenceTaskPattern = appViewModel.recurrenceTaskPattern
    val numDaysNewTask = appViewModel.numDaysNewTask
    val selectedWeekDaysNewTask = appViewModel.selectedWeekDaysNewTask
    val numWeeksNewTask = appViewModel.numWeeksNewTask
    val selectedMonthDaysNewTask = appViewModel.selectedMonthDaysNewTask
    val numMonthsNewTask = appViewModel.numMonthsNewTask
    val selectedYearDaysNewTask = appViewModel.selectedYearDaysNewTask
    val numYearsNewTask = appViewModel.numYearsNewTask
    val selectedCustomIntervalNewTask = appViewModel.selectedCustomIntervalNewTask
    val numTimesNewTask = appViewModel.numTimesNewTask


    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            Text(
                text = "Recurring",
                color = themeColors.text1
            )

            Text(
                text = if (isTaskRecurring) "ON" else "OFF",
                modifier = Modifier
                    .clickable {
                        appViewModel.toggleIsTaskRecurring()
                        Log.d("isTaskRecurring", isTaskRecurring.toString())
                    }
            )
        }

        if(isTaskRecurring) {
            Text(
                text = "Recurrence: $recurrenceTaskPattern",
                color = themeColors.text1,
            )

            LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(listOf("daily", "weekly", "monthly", "yearly", "custom")) { recurrence ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(color = themeColors.backGround1)
                            .clickable {
                                appViewModel.updateRecurrenceTaskPattern(recurrence)
                            }
                    ) {
                        Text(
                            text = recurrence,
                            color = if(recurrence == recurrenceTaskPattern) {
                                themeColors.tabButtonSelected
                            } else {
                                themeColors.tabButtonDefault
                            }
                        )
                    }

                }
            }
        }

        when (recurrenceTaskPattern) {
            "daily" -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Num. de días",
                        color = themeColors.text1
                    )

                    // Input para ingresar el número de días
                    TextField(
                        value = numDaysNewTask.toString(),
                        onValueChange = {
                            // Solo aceptar valores numéricos
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                appViewModel.updateNumDaysNewTask(if (it.isEmpty()) 0 else it.toInt())
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.width(100.dp)
                    )
                }
            }


            "weekly" -> {
                val weekDays = listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")

                // LazyRow para seleccionar los días de la semana
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(weekDays.indices.toList()) { index ->
                        val day = weekDays[index]
                        val isSelected = selectedWeekDaysNewTask.contains(index)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) themeColors.backGround3 else themeColors.backGround1)
                                .clickable {
                                    if (isSelected) {
                                        appViewModel.updateSelectedWeekDaysNewTask(selectedWeekDaysNewTask.toMutableList().apply {
                                            remove(index)
                                        })
                                    } else {
                                        appViewModel.updateSelectedWeekDaysNewTask(selectedWeekDaysNewTask.toMutableList().apply {
                                            add(index)
                                        })
                                    }
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                color = if (isSelected) themeColors.tabButtonSelected else themeColors.text1,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Input para ingresar el número de semanas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Num. de semanas",
                        color = themeColors.text1
                    )

                    // Input para el número de semanas
                    TextField(
                        value = numWeeksNewTask.toString(),
                        onValueChange = {
                            // Solo aceptar valores numéricos
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                appViewModel.updateNumWeeksNewTask(if (it.isEmpty()) 0 else it.toInt())
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.width(100.dp)
                    )
                }
            }



            "monthly" -> {
                val days = (1..31).map { it.toString() } + "Último día del mes"


                // LazyRow para seleccionar los días del mes
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(days) { day ->
                        val isSelected = selectedMonthDaysNewTask.contains(if (day == "Último día del mes") -1 else day.toInt())

                        Text(
                            text = day,
                            color = if (isSelected) themeColors.tabButtonSelected else themeColors.text1,
                            modifier = Modifier
                                .background(if (isSelected) themeColors.backGround3 else themeColors.backGround1)
                                .clickable {
                                    val value = if (day == "Último día del mes") -1 else day.toInt()
                                    if (isSelected) {
                                        appViewModel.updateSelectedMonthDaysNewTask(
                                            selectedMonthDaysNewTask.toMutableList().apply {
                                            remove(value) }
                                        )
                                    } else {
                                        appViewModel.updateSelectedMonthDaysNewTask(
                                            selectedMonthDaysNewTask.toMutableList().apply {
                                            add(value) }
                                        )
                                    }
                                }
                                .padding(8.dp)
                        )
                    }
                }

                // Input para ingresar el número de meses
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Num. de meses",
                        color = themeColors.text1
                    )

                    // Input para el número de meses
                    TextField(
                        value = numMonthsNewTask.toString(),
                        onValueChange = {
                            // Solo aceptar valores numéricos
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                appViewModel.updateNumMonthsNewTask(if (it.isEmpty()) 0 else it.toInt())
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.width(100.dp)
                    )
                }
            }


            "yearly" -> {
                val months = listOf(
                    "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                    "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
                )
                val daysInMonth = listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31) // Febrero siempre con 29 días

                var selectedMonthIndex by remember { mutableStateOf(-1) }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Fila de meses (LazyRow)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        items(months.indices.toList()) { index ->
                            val isSelected = selectedMonthIndex == index
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color.Gray else Color.LightGray)
                                    .clickable { selectedMonthIndex = index }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = months[index],
                                    color = if (isSelected) Color.White else Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Fila de días (LazyRow) solo si un mes ha sido seleccionado
                    if (selectedMonthIndex != -1) {
                        val days = (1..daysInMonth[selectedMonthIndex]).toList()

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            items(days) { day ->
                                val formattedDate = String.format(Locale.US, "%02d/%02d", selectedMonthIndex + 1, day)
                                val isSelected = selectedYearDaysNewTask.contains(formattedDate)

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Color.Gray else Color.LightGray)
                                        .clickable {
                                            appViewModel.updateSelectedYearDaysNewTask(
                                                selectedYearDaysNewTask.toMutableSet().apply {
                                                    if (isSelected) remove(formattedDate) else add(formattedDate)
                                                }
                                            )
                                            Log.d("selectedYearDays", selectedYearDaysNewTask.toString())
                                        }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.toString(),
                                        color = if (isSelected) Color.White else Color.Black,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Cantidad de años
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Num. de años",
                            color = themeColors.text1
                        )

                        // Input para el número de años
                        TextField(
                            value = numYearsNewTask.toString(),
                            onValueChange = {
                                // Solo aceptar valores numéricos
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    appViewModel.updateNumYearsNewTask(if (it.isEmpty()) 0 else it.toInt())
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }
            }


            "custom" -> {
                var expanded by remember { mutableStateOf(false) }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Caja para mostrar el intervalo de días
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                            .background(themeColors.backGround1)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Cada ${selectedCustomIntervalNewTask} días",
                            color = themeColors.text1
                        )
                    }

                    // DropdownMenu para elegir el intervalo de días (1-30)
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        (1..30).forEach { interval ->
                            DropdownMenuItem(
                                text = { Text(text = "$interval días") },
                                onClick = {
                                    appViewModel.updateSelectedCustomIntervalNewTask(interval)
                                    expanded = false
                                }
                            )
                        }
                    }

                    // TextField para ingresar el número de repeticiones
                    TextField(
                        value = numTimesNewTask.toString(),
                        onValueChange = {
                            // Asegurarse de que solo se ingresen valores numéricos
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                appViewModel.updateNumTimesNewTask(if (it.isEmpty()) 0 else it.toInt())
                            }
                        },
                        label = { Text(text = "Número de repeticiones") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }



        }
    }
}