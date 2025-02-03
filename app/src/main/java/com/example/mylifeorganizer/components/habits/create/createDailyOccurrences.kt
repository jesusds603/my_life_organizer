package com.example.mylifeorganizer.components.habits.create

import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Función para crear ocurrencias diarias
fun createDailyOccurrences(habitId: Long, time: String, noteViewModel: NoteViewModel) {
    val calendar = Calendar.getInstance() // Fecha actual
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) // Formato de fecha

    for (i in 0 until 365) { // Crear ocurrencias para 1 año
        val date = dateFormat.format(calendar.time) // Formatear la fecha como "yyyy/MM/dd"
        noteViewModel.insertHabitOccurrence(
            HabitOccurrenceEntity(
                habitId = habitId,
                date = date,
                time = time
            )
        )
        calendar.add(Calendar.DAY_OF_YEAR, 1) // Siguiente día
    }
}
