package com.example.mylifeorganizer.components.habits.create

import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// Función para crear ocurrencias semanales
fun createWeeklyOccurrences(
    habitId: Long,
    time: String,
    isAnytime: Boolean,
    numDays: Int,
    recurrenceDays: String,
    noteViewModel: NoteViewModel
) {
    val calendar = Calendar.getInstance() // Fecha actual
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) // Formato de fecha

    for (i in 0 until 52) { // Crear ocurrencias para 1 año (52 semanas)
        if (isAnytime) {
            // Obtener el primer día de la semana (domingo)
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val startOfWeek = dateFormat.format(calendar.time)

            // Obtener el último día de la semana (sábado)
            calendar.add(Calendar.DAY_OF_YEAR, 6)
            val endOfWeek = dateFormat.format(calendar.time)

            // Crear el rango de fechas para la semana
            val dateRange = "$startOfWeek-$endOfWeek"

            // Crear numDays ocurrencias para esta semana
            repeat(numDays) {
                noteViewModel.insertHabitOccurrence(
                    HabitOccurrenceEntity(
                        habitId = habitId,
                        date = dateRange, // Usar el rango de fechas
                        time = time
                    )
                )
            }
        } else {
            // Crear ocurrencias en los días específicos de la semana
            recurrenceDays.split(",").forEach { dayIndex ->
                calendar.set(Calendar.DAY_OF_WEEK, dayIndex.toInt() + 1) // Ajustar al día de la semana
                val date = dateFormat.format(calendar.time) // Formato "yyyy/MM/dd"
                noteViewModel.insertHabitOccurrence(
                    HabitOccurrenceEntity(
                        habitId = habitId,
                        date = date,
                        time = time
                    )
                )
            }
        }
        calendar.add(Calendar.WEEK_OF_YEAR, 1) // Siguiente semana
    }
}