package com.example.mylifeorganizer.components.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun getDateAfterDays(startDate: String, daysToAdd: Int): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val date = LocalDate.parse(startDate, formatter)
    return date.plusDays(daysToAdd.toLong()).format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getWeekDayOccurrenceDates(startDate: String, weekDays: List<Int>, numWeeks: Int): List<String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val start = LocalDate.parse(startDate, formatter)
    val occurrences = mutableListOf<String>()

    for (week in 0 until numWeeks) {
        for (dayIndex in weekDays) {
            val dayOfWeek = java.time.DayOfWeek.of(dayIndex + 1)
            val dayOffset = start.with(java.time.temporal.TemporalAdjusters.nextOrSame(dayOfWeek)).plusWeeks(week.toLong())
            occurrences.add(dayOffset.format(formatter))
        }
    }
    return occurrences
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthlyOccurrenceDates(startDate: String, monthDays: List<Int>, numMonths: Int): List<String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val start = LocalDate.parse(startDate, formatter)
    val occurrences = mutableListOf<String>()

    for (month in 0 until numMonths) {
        for (day in monthDays) {
            val dateInMonth = start.withMonth(start.monthValue + month).withDayOfMonth(day)
            occurrences.add(dateInMonth.format(formatter))
        }
    }
    return occurrences
}

@RequiresApi(Build.VERSION_CODES.O)
fun getYearlyOccurrenceDates(startDate: String, yearDays: List<String>, numYears: Int): List<String> {
    val formatter = DateTimeFormatter.ofPattern("MM/dd")
    val start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    val occurrences = mutableListOf<String>()

    for (year in 0 until numYears) {
        for (yearDay in yearDays) {
            val date = LocalDate.parse("${start.year + year}/${yearDay}", DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            occurrences.add(date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
        }
    }
    return occurrences
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCustomOccurrenceDates(startDate: String, interval: Int, numTimes: Int): List<String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val start = LocalDate.parse(startDate, formatter)
    val occurrences = mutableListOf<String>()

    for (i in 0 until numTimes) {
        occurrences.add(start.plusDays(i * interval.toLong()).format(formatter))
    }
    return occurrences
}
