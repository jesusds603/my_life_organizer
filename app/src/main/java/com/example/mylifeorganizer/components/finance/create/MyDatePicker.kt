package com.example.mylifeorganizer.components.finance.create

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyDatePicker() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Convertir selectedDueDate a LocalDate o usar la fecha actual si está vacío
    val initialDate = LocalDate.now()

    // Estado interno para manejar la fecha seleccionada en el DatePicker
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentYear by remember { mutableIntStateOf(selectedDate.year) }
    var currentMonth by remember { mutableStateOf(selectedDate.month) }

    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
    val daysInMonth = currentMonth.length(Year.of(currentYear).isLeap)
    // Indice del dia de la semana de 0 a 6
    val firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1).dayOfWeek.value % 7
    println("first day of mont ${LocalDate.of(currentYear, currentMonth, 1).dayOfWeek.value}")
    val daysGrid = (1..daysInMonth).toList()
    // Crearemos una lista para guardar los ultimos dias de la cuadricula vacios
    val numDaysEndMonth = (7*7 - (firstDayOfMonth + daysInMonth)) % 7 // 7 semanas

    AlertDialog(
        onDismissRequest = { appViewModel.toggleAddingDateForFinance() },
        title = {
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                style = MaterialTheme.typography.titleLarge,
                color = themeColors.text1,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Selector de año
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { currentYear -= 1 }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Year")
                    }
                    Text(
                        text = currentYear.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = themeColors.text1
                    )
                    IconButton(onClick = { currentYear += 1 }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Year")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Selector de mes
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        currentMonth = currentMonth.minus(1)
                        if (currentMonth.value < 1) {
                            currentMonth = Month.DECEMBER
                            currentYear -= 1
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                    }
                    Text(
                        text = currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                        style = MaterialTheme.typography.titleLarge,
                        color = themeColors.text1
                    )
                    IconButton(onClick = {
                        currentMonth = currentMonth.plus(1)
                        if (currentMonth.value > 12) {
                            currentMonth = Month.JANUARY
                            currentYear += 1
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Días de la semana
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
                        .background(themeColors.backGround2)
                        .padding(vertical = 8.dp)
                    ,
                ) {
                    daysOfWeek.forEachIndexed { index, day ->
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = if (selectedDate.dayOfWeek.value % 7 == index) themeColors.tabButtonSelected else themeColors.text1,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cuadrícula del calendario
                // Crea una lista de "" de cantidad firstDayOfMonth que son los dias vacios
                val gridItems = List(firstDayOfMonth) { "" } + daysGrid.map { it.toString() } + List(numDaysEndMonth) {""}
                val rows = gridItems.chunked(7)

                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rows.forEach { week ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(40.dp)
                                        .background(
                                            if (day.isNotEmpty() && day.toInt() == selectedDate.dayOfMonth
                                                && currentMonth == selectedDate.month
                                                && currentYear == selectedDate.year
                                            ) themeColors.tabButtonSelected
                                            else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            if (day.isNotEmpty()) {
                                                selectedDate = LocalDate.of(
                                                    currentYear,
                                                    currentMonth,
                                                    day.toInt()
                                                )
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day,
                                        textAlign = TextAlign.Center,
                                        color = themeColors.text1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                    appViewModel.updateDateForNewFinance(formattedDate)
                    appViewModel.toggleAddingDateForFinance()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { appViewModel.toggleAddingDateForFinance() }) {
                Text("Cancel")
            }
        },
        containerColor = themeColors.backGround1
    )
}