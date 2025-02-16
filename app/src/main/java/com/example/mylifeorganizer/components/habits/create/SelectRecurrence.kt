package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.habits.create.options.MonthlyOptions
import com.example.mylifeorganizer.components.habits.create.options.WeeklyOptions
import com.example.mylifeorganizer.components.habits.create.options.YearlyOptions
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectRecurrence() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val recurrencePatternForNewHabit = appViewModel.recurrencePatternForNewHabit.value // "daily", "weekly", "monthly", "yearly"

    var showDetails by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = if(isLangEng) "Select the recurrence pattern" else "Selecciona el patrón de recurrencia",
            color = themeColors.text1
        )

        fun getTranslatedRecurrenceType(recurrenceType: String): String {
            return when (recurrenceType) {
                "daily" -> if (isLangEng) "Daily" else "Diario"
                "weekly" -> if (isLangEng) "Weekly" else "Semanal"
                "monthly" -> if (isLangEng) "Monthly" else "Mensual"
                "yearly" -> if (isLangEng) "Yearly" else "Anual"
                else -> recurrenceType // Por si acaso hay un valor no esperado
            }
        }

        // LazyRow para los botones de recurrencia
        LazyRow {
            items(listOf("daily", "weekly", "monthly", "yearly")) { recurrenceType ->
                val isSelected = recurrencePatternForNewHabit == recurrenceType
                Button(
                    onClick = {
                        showDetails = true
                        appViewModel.changeRecurrencePatternForNewHabit(recurrenceType)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color.Magenta else themeColors.backGround1
                    )
                ) {
                    Text(
                        text = getTranslatedRecurrenceType(recurrenceType), // Texto traducido
                        color = themeColors.text1
                    )
                }
            }
        }

        if(showDetails) {
            // Dependiendo de la selección, mostramos las opciones adicionales
            when (recurrencePatternForNewHabit) {
                "weekly" -> {
                    WeeklyOptions()
                }
                "monthly" -> {
                    MonthlyOptions()
                }
                "yearly" -> {
                    YearlyOptions()
                }
            }

            Button(
                onClick = { showDetails = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Magenta
                )
            ) {
                Text(
                    text = "OK",
                    color = themeColors.text1
                )
            }
        }
    }
}

