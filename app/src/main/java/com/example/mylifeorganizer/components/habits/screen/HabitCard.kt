package com.example.mylifeorganizer.components.habits.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.HabitEntity
import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitCard(
    habit: HabitEntity,
    occurrence: HabitOccurrenceEntity,
    isRange: Boolean,
    occurrences: List<HabitOccurrenceEntity>,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 2.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showMenu = true
                    },
                    onTap = {}
                )
            }
    ) {
        // Primera columna: Flechas para aumentar/disminuir tareas completadas
        Column(
            modifier = Modifier
                .fillMaxWidth(0.1f)
                .fillMaxHeight()
                .background(themeColors.bgCardNote),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isRange) {
                // Mostrar flechas solo para rangos
                val totalHabits = when (habit.recurrencePattern) {
                    "weekly" -> habit.numDaysForWeekly
                    "monthly" -> habit.numDaysForMonthly
                    "yearly" -> habit.numDaysForYearly
                    else -> 1
                }
                val completedOccurrences = occurrences.count {
                    it.habitId == habit.habitId && it.isCompleted && it.date.contains("-")
                }

                // Flecha hacia arriba
                IconButton(
                    onClick = {
                        if (completedOccurrences < totalHabits) {
                            // Encontrar la primera ocurrencia no completada y marcarla como completada
                            val occurrenceToUpdate = occurrences.firstOrNull { !it.isCompleted }
                            occurrenceToUpdate?.let {
                                noteViewModel.updateHabitOccurrence(it.copy(isCompleted = true))
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                        contentDescription = "Aumentar",
                        tint = if (completedOccurrences < totalHabits) themeColors.text1 else Color.Gray
                    )
                }

                // Flecha hacia abajo
                IconButton(
                    onClick = {
                        if (completedOccurrences > 0) {
                            // Encontrar la última ocurrencia completada y marcarla como no completada
                            val occurrenceToUpdate = occurrences.lastOrNull { it.isCompleted }
                            occurrenceToUpdate?.let {
                                noteViewModel.updateHabitOccurrence(it.copy(isCompleted = false))
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                        contentDescription = "Disminuir",
                        tint = if (completedOccurrences > 0) themeColors.text1 else Color.Gray
                    )
                }
            } else {
                // Mostrar el icono de selección para fechas únicas
                IconButton(
                    onClick = {
                        noteViewModel.updateHabitOccurrence(occurrence.copy(isCompleted = !occurrence.isCompleted))
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (occurrences.first().isCompleted) {
                                R.drawable.baseline_check_box_24
                            } else {
                                R.drawable.baseline_check_box_outline_blank_24
                            }
                        ),
                        contentDescription = null,
                        tint = if (occurrences.first().isCompleted) Color.Green else Color.Gray
                    )
                }
            }
        }

        // Segunda columna: Título y fecha
        Column(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight()
                .background(
                    color = themeViewModel.getCategoryColor(habit.color)
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = habit.title,
                color = themeColors.text1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isRange) occurrences.first().date else occurrences.first().date,
                color = themeColors.text1,
                fontSize = 12.sp
            )
        }

        // Mostrar el emoji correspondiente al tiempo
        val timeEmoji = when {
            occurrences.first().time == "any" -> "☀️🌒"
            occurrences.first().time == "morning" -> "\uD83C\uDF05"
            occurrences.first().time == "afternoon" -> "\uD83C\uDF15"
            occurrences.first().time == "night" -> "\uD83C\uDF19"
            occurrences.first().time.matches(Regex("\\d{2}:\\d{2}")) -> {
                val hour = occurrences.first().time.substring(0, 2).toInt()
                when {
                    hour in 6..11 -> "🌅" // Mañana: 6:00 - 11:59
                    hour in 12..17 -> "🌇" // Tarde: 12:00 - 17:59
                    hour in 18..23 || hour in 0..5 -> "🌆" // Noche: 18:00 - 5:59
                    else -> "☀️🌒" // Por defecto
                }
            }
            else -> "☀️🌒" // Por defecto, en caso de un valor inesperado
        }

        // Tercera columna: Progreso o fecha
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight()
                .background(themeColors.bgCardNote)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isRange) {
                // Mostrar el progreso de tareas completadas
                val totalHabits = when (habit.recurrencePattern) {
                    "weekly" -> habit.numDaysForWeekly
                    "monthly" -> habit.numDaysForMonthly
                    "yearly" -> habit.numDaysForYearly
                    else -> 1
                }
                val completedHabits = occurrences.count {
                    it.habitId == habit.habitId && it.isCompleted && it.date.contains("-")
                }

                Row {
                    Text(
                        text = "$completedHabits/$totalHabits",
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Text(
                        text = timeEmoji,
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else {

                fun getTranslatedTime(time: String): String {
                    return when (time) {
                        "any" -> if (isLangEng) "Anytime" else "Cuando sea"
                        "morning" -> if (isLangEng) "Morning" else "Mañana"
                        "afternoon" -> if (isLangEng) "Afternoon" else "Tarde"
                        "night" -> if (isLangEng) "Night" else "Noche"
                        else -> time // Para el caso de "hh:mm", se devuelve el valor tal cual
                    }
                }

                Row {
                    Text(
                        text = getTranslatedTime(occurrences.first().time),
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = timeEmoji,
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            fun getTranslatedRecurrencePattern(pattern: String): String {
                return when (pattern) {
                    "daily" -> if (isLangEng) "Daily" else "Diario"
                    "weekly" -> if (isLangEng) "Weekly" else "Semanal"
                    "monthly" -> if (isLangEng) "Monthly" else "Mensual"
                    "yearly" -> if (isLangEng) "Yearly" else "Anual"
                    else -> pattern // Por si acaso hay un valor no esperado
                }
            }

            Text(
                text = getTranslatedRecurrencePattern(habit.recurrencePattern),
                color = themeColors.text1,
                fontSize = 12.sp
            )
        }

        FloatingOptionsHabit(
            showMenu = showMenu,
            changeShowMenu = { showMenu = it },
            habit = habit,
            occurrences = occurrences
        )
    }
}