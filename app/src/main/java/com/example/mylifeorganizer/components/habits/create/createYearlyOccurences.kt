package com.example.mylifeorganizer.components.habits.create

import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// Función para crear ocurrencias anuales
fun createYearlyOccurrences(
    habitId: Long,
    time: String,
    isAnytime: Boolean,
    numDays: Int,
    recurrenceDays: String,
    noteViewModel: NoteViewModel
) {
    val calendar = Calendar.getInstance() // Fecha actual
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) // Formato de fecha

    if (isAnytime) {
        // Crear un rango anual
        calendar.set(Calendar.DAY_OF_YEAR, 1) // Primer día del año
        val startDate = dateFormat.format(calendar.time)

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR)) // Último día del año
        val endDate = dateFormat.format(calendar.time)

        val dateRange = "$startDate-$endDate"

        // Crear numDays ocurrencias para el año
        repeat(numDays) {
            noteViewModel.insertHabitOccurrence(
                HabitOccurrenceEntity(
                    habitId = habitId,
                    date = dateRange, // Rango anual
                    time = time
                )
            )
        }
    } else {
        // Crear ocurrencias en los días específicos del año
        recurrenceDays.split(",").forEach { day ->
            val (monthStr, dayOfMonthStr) = day.split("/")
            val month = monthStr.toInt() - 1 // Convertir a índice 0-based
            val dayOfMonth = dayOfMonthStr.toInt()

            // Ajustar al mes y día del mes
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Verificar si la fecha es válida (por ejemplo, 30 de febrero no existe)
            if (calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
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
    }
}