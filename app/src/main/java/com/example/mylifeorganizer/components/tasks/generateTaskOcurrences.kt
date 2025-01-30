package com.example.mylifeorganizer.components.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mylifeorganizer.room.TaskOccurrenceEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

@RequiresApi(Build.VERSION_CODES.O)
fun generateTaskOccurrences(
    taskId: Long,
    dueDate: String,
    dueTime: String,
    isRecurring: Boolean,
    recurrencePattern: String,
    numDays: Int,
    recurrenceWeekDays: List<Int>,
    numWeeks: Int,
    recurrenceMonthDays: List<Int>,
    numMonths: Int,
    recurrenceYearDays: Set<String>,
    numYears: Int,
    recurrenceInterval: Int,
    numTimes: Int
): List<TaskOccurrenceEntity> {
    val occurrences = mutableListOf<TaskOccurrenceEntity>()

    // Formateador de fechas
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val initialDate = LocalDate.parse(dueDate, dateFormatter)

    if (!isRecurring) {
        // Si no es recurrente, solo se agrega una ocurrencia
        occurrences.add(
            TaskOccurrenceEntity(
                taskId = taskId,
                dueDate = dueDate,
                dueTime = dueTime
            )
        )
    } else {
        when (recurrencePattern) {
            "daily" -> {
                // Ocurrencias diarias
                for (i in 0 until numDays) {
                    val newDate = initialDate.plusDays(i.toLong())
                    occurrences.add(
                        TaskOccurrenceEntity(
                            taskId = taskId,
                            dueDate = newDate.format(dateFormatter),
                            dueTime = dueTime
                        )
                    )
                }
            }
            "weekly" -> {
                // Ocurrencias semanales
                for (week in 0 until numWeeks) {
                    recurrenceWeekDays.forEach { dayOfWeek ->
                        val newDate = initialDate
                            .plusWeeks(week.toLong())
                            .with(ChronoField.DAY_OF_WEEK, dayOfWeek.toLong())
                        occurrences.add(
                            TaskOccurrenceEntity(
                                taskId = taskId,
                                dueDate = newDate.format(dateFormatter),
                                dueTime = dueTime
                            )
                        )
                    }
                }
            }
            "monthly" -> {
                // Ocurrencias mensuales
                for (month in 0 until numMonths) {
                    recurrenceMonthDays.forEach { dayOfMonth ->
                        val newDate = initialDate
                            .plusMonths(month.toLong())
                            .withDayOfMonth(dayOfMonth)
                        occurrences.add(
                            TaskOccurrenceEntity(
                                taskId = taskId,
                                dueDate = newDate.format(dateFormatter),
                                dueTime = dueTime
                            )
                        )
                    }
                }
            }
            "yearly" -> {
                // Ocurrencias anuales
                for (year in 0 until numYears) {
                    recurrenceYearDays.forEach { monthDay ->
                        val monthDayParts = monthDay.split("/")
                        val month = monthDayParts[0].toInt()
                        val day = monthDayParts[1].toInt()
                        val newDate = initialDate
                            .plusYears(year.toLong())
                            .withMonth(month)
                            .withDayOfMonth(day)
                        occurrences.add(
                            TaskOccurrenceEntity(
                                taskId = taskId,
                                dueDate = newDate.format(dateFormatter),
                                dueTime = dueTime
                            )
                        )
                    }
                }
            }
            "custom" -> {
                // Ocurrencias personalizadas (intervalo de días)
                for (i in 0 until numTimes) {
                    val newDate = initialDate.plusDays((i * recurrenceInterval).toLong())
                    occurrences.add(
                        TaskOccurrenceEntity(
                            taskId = taskId,
                            dueDate = newDate.format(dateFormatter),
                            dueTime = dueTime
                        )
                    )
                }
            }
        }
    }

    return occurrences
}