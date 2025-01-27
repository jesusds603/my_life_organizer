package com.example.mylifeorganizer.components.tasks.create

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePicker(
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentYear by remember { mutableStateOf(initialDate.year) }
    var currentMonth by remember { mutableStateOf(initialDate.month) }

    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
    val daysInMonth = currentMonth.length(Year.of(currentYear).isLeap)
    val firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1).dayOfWeek.value % 7
    val daysGrid = (1..daysInMonth).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                style = MaterialTheme.typography.titleLarge,
                color = themeColors.text1,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Year Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { currentYear -= 1 }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Year")
                    }
                    Text(text = currentYear.toString(), style = MaterialTheme.typography.titleLarge, color = themeColors.text1)
                    IconButton(onClick = { currentYear += 1 }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Year")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Month Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        currentMonth = currentMonth.minus(1)
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
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Days of the Week
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    daysOfWeek.forEach { day ->
                        Text(text = day, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = themeColors.text1)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar Grid
                val gridItems = List(firstDayOfMonth) { "" } + daysGrid.map { it.toString() }
                val rows = gridItems.chunked(7)

                Column {
                    rows.forEach { week ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
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
                                    Text(text = day, textAlign = TextAlign.Center, color = themeColors.text1)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = themeColors.backGround1
    )
}