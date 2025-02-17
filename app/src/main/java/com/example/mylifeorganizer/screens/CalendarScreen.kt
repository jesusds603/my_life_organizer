package com.example.mylifeorganizer.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val todayDate = LocalDate.now()
    val selectedDateCalendar = appViewModel.selectedDateCalendar.value
    var selectedMonth by remember { mutableStateOf(selectedDateCalendar.month) }
    var selectedYear by remember { mutableIntStateOf(selectedDateCalendar.year) }

    val daysOfWeek = if(isLangEng) {
        listOf("S", "M", "T", "W", "T", "F", "S")
    } else {
        listOf("D", "L", "M", "M", "J", "V", "S")
    }
    val daysInMonth = selectedMonth.length(Year.of(selectedYear).isLeap)
    val firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth, 1).dayOfWeek.value % 7
    val daysGrid = (1..daysInMonth).toList()
    // Crearemos una lista para guardar los ultimos dias de la cuadricula vacios
    val numDaysEndMonth = (6*7 - (firstDayOfMonth + daysInMonth)) % 7 // 6 semanas


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Selector de año
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { selectedYear -= 1 },
                modifier = Modifier
                    .background(
                        color = themeColors.backGround3,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous Year",
                    tint = themeColors.text1
                )
            }
            Text(
                text = selectedYear.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = themeColors.text1
            )
            IconButton(
                onClick = { selectedYear += 1 },
                modifier = Modifier
                    .background(
                        color = themeColors.backGround3,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next Year",
                    tint = themeColors.text1
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Selector de mes
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    selectedMonth = selectedMonth.minus(1)
                    if (selectedMonth.value < 1) {
                        selectedMonth = Month.DECEMBER
                        selectedYear -= 1
                    }
                },
                modifier = Modifier
                    .background(
                        color = themeColors.backGround3,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous Month",
                    tint = themeColors.text1
                )
            }
            Text(
                text = selectedMonth.getDisplayName(TextStyle.FULL, if (isLangEng) Locale.ENGLISH else Locale("es", "MX")),
                style = MaterialTheme.typography.titleLarge,
                color = themeColors.text1
            )
            IconButton(
                onClick = {
                    selectedMonth = selectedMonth.plus(1)
                    if (selectedMonth.value > 12) {
                        selectedMonth = Month.JANUARY
                        selectedYear += 1
                    }
                },
                modifier = Modifier
                    .background(
                        color = themeColors.backGround3,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next Month",
                    tint = themeColors.text1
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Días de la semana
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp)
                .background(themeColors.backGround4)
                .padding(vertical = 8.dp),
        ) {
            daysOfWeek.forEachIndexed { index, day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = if (selectedDateCalendar.dayOfWeek.value % 7 == index) themeColors.tabButtonSelected else themeColors.text1,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cuadrícula del calendario
        // Crea una lista de "" de cantidad firstDayOfMonth que son los dias vacios
        val gridItems =
            List(firstDayOfMonth) { "" } + daysGrid.map { it.toString() } + List(numDaysEndMonth) { "" }
        val rows = gridItems.chunked(7)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            rows.forEach { week ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    week.forEach { day ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    if (day.isNotEmpty() && day.toInt() == selectedDateCalendar.dayOfMonth
                                        && selectedMonth == selectedDateCalendar.month
                                        && selectedYear == selectedDateCalendar.year
                                    ) {
                                        themeColors.tabButtonSelected
                                    } else if (day.isNotEmpty() && day == todayDate.dayOfMonth.toString() && selectedMonth == todayDate.month && selectedYear == todayDate.year) {
                                        themeColors.backGround3
                                    } else {
                                        Color.Transparent
                                    },
                                )
                                .border(
                                    width = 1.dp,
                                    color =  if (day.isNotEmpty() && day == todayDate.dayOfMonth.toString() && selectedMonth == todayDate.month && selectedYear == todayDate.year) {
                                        Color.Magenta
                                    }
                                    else {
                                        themeColors.backGround4
                                    }
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            if (day.isNotEmpty()) {
                                                appViewModel.toggleShowingDayCalendar()

                                                appViewModel.changeSelectedDateCalendar(
                                                    LocalDate.of(
                                                        selectedYear,
                                                        selectedMonth,
                                                        day.toInt()
                                                    )
                                                )
                                            }
                                        },
                                        onTap = {
                                            if (day.isNotEmpty()) {
                                                appViewModel.changeSelectedDateCalendar(
                                                    LocalDate.of(
                                                        selectedYear,
                                                        selectedMonth,
                                                        day.toInt()
                                                    )
                                                )
                                            }
                                        },
                                        onDoubleTap = {
                                            if (day.isNotEmpty()) {
                                                appViewModel.toggleShowingDayCalendar()

                                                appViewModel.changeSelectedDateCalendar(
                                                    LocalDate.of(
                                                        selectedYear,
                                                        selectedMonth,
                                                        day.toInt()
                                                    )
                                                )
                                            }
                                        }
                                    )
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
}