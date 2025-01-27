package com.example.mylifeorganizer.components.tasks.create

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Funciones para formatear la fecha y la hora
fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

fun formatTime(timestamp: Long): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(Date(timestamp))
}
