package com.example.mylifeorganizer.components.habits.create

import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// Función para crear ocurrencias mensuales
fun createMonthlyOccurrences(
    habitId: Long,
    time: String,
    isAnytime: Boolean,
    numDays: Int,
    recurrenceDays: String,
    noteViewModel: NoteViewModel
) {
    val calendar = Calendar.getInstance() // Fecha actual
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) // Formato de fecha

    for (i in 0 until 12) { // Crear ocurrencias para 1 año (12 meses)
        if (isAnytime) {
            // Establecer el primer día del mes
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startDate = dateFormat.format(calendar.time)

            // Obtener el último día del mes
            val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            calendar.set(Calendar.DAY_OF_MONTH, lastDay)
            val endDate = dateFormat.format(calendar.time)

            val dateRange = "$startDate-$endDate"

            // Crear numDays ocurrencias para este mes
            repeat(numDays) {
                noteViewModel.insertHabitOccurrence(
                    HabitOccurrenceEntity(
                        habitId = habitId,
                        date = dateRange, // Rango mensual
                        time = time
                    )
                )
            }
        } else {
            // Crear ocurrencias en los días específicos del mes
            recurrenceDays.split(",").forEach { dayIndexStr ->
                val dayIndex = dayIndexStr.toInt()
                val dayOfMonth = dayIndex + 1 // Convertir a día 1-based

                // Asegurar que el día no exceda los días del mes
                val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                val clampedDay = minOf(dayOfMonth, maxDay)

                calendar.set(Calendar.DAY_OF_MONTH, clampedDay)
                val date = dateFormat.format(calendar.time)
                noteViewModel.insertHabitOccurrence(
                    HabitOccurrenceEntity(
                        habitId = habitId,
                        date = date, // Fecha específica
                        time = time
                    )
                )
            }
        }
        // Mover al primer día del próximo mes
        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
    }
}