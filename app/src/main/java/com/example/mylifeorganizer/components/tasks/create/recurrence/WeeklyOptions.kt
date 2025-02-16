package com.example.mylifeorganizer.components.tasks.create.recurrence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyOptions(
    selectedWeekDaysNewTask: List<Int>,
    numWeeksNewTask: Int,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val weekDays = if(isLangEng) {
        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    } else {
        listOf("Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab")
    }

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
                    .background(if (isSelected) themeColors.bgCardNote else themeColors.backGround1)
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
            text = if(isLangEng) "Num. of weeks" else "Núm. de semanas",
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