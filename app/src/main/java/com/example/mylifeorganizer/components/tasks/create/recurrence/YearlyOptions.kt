package com.example.mylifeorganizer.components.tasks.create.recurrence

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YearlyOptions(
    selectedYearDaysNewTask: Set<String>,
    numYearsNewTask: Int,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value


    val months = if(isLangEng) {
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    } else {
        listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
    }
    val daysInMonth = listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31) // Febrero siempre con 29 días

    var selectedMonthIndex by remember { mutableIntStateOf(-1) }

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
                        .background(if (isSelected) themeColors.bgCardNote else themeColors.backGround1)
                        .clickable { selectedMonthIndex = index }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = months[index],
                        color = themeColors.text1,
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
                            .background(if (isSelected) themeColors.bgCardNote else themeColors.backGround1)
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
                            color = themeColors.text1,
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
                text = if(isLangEng) "Num. of years" else "Núm. de años",
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
                modifier = Modifier.width(100.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = themeColors.text1,
                    unfocusedTextColor = themeColors.text1,
                    focusedContainerColor = themeColors.backGround1,
                    unfocusedContainerColor = themeColors.backGround1
                )
            )
        }
    }
}