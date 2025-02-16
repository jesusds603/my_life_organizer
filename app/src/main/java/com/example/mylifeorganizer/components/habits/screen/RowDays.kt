package com.example.mylifeorganizer.components.habits.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowDays(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val daysOfWeek = if(isLangEng) {
        listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    } else {
        listOf("DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB")
    }
    val calendar = Calendar.getInstance()
    val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // Día actual de la semana (1 = Domingo, 2 = Lunes, etc.)
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    // Estado para la semana seleccionada
    var selectedWeekOffset by remember { mutableStateOf(0) }

    // Calcular las fechas de la semana actual
    val weekDates = remember(selectedWeekOffset) {
        val startOfWeek = calendar.clone() as Calendar
        startOfWeek.add(Calendar.DAY_OF_YEAR, -currentDayOfWeek + 1 + selectedWeekOffset * 7)
        (0 until 7).map { offset ->
            val date = startOfWeek.clone() as Calendar
            date.add(Calendar.DAY_OF_YEAR, offset)
            dateFormat.format(date.time)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Fila fija con los días de la semana
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    color = themeColors.text1,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Fila dinámica con las fechas de la semana
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            weekDates.forEach { date ->
                val isCurrentDay = date == dateFormat.format(Calendar.getInstance().time)
                Text(
                    text = date.split("/").last(), // Mostrar solo el día (DD)
                    color = if (isCurrentDay) Color.Magenta else themeColors.text1,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .background(
                            color = if (date == selectedDate) {
                                themeColors.bgCardFolder
                            } else {
                                Color.Transparent
                            }
                        )
                        .weight(1f)
                        .clickable { onDateSelected(date) },
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        // Botones para cambiar de semana
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { selectedWeekOffset-- },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.bgCardFolder
                )
            ) {
                Text(
                    text = if (isLangEng) {
                        "Last week"
                    } else {
                        "Semana anterior"
                    },
                    color = themeColors.text1
                )
            }
            Button(
                onClick = { selectedWeekOffset++ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.bgCardFolder
                )
            ) {
                Text(
                    text = if (isLangEng) {
                        "Next week"
                    } else {
                        "Siguiente semana"
                    },
                    color = themeColors.text1
                )
            }
        }
    }
}