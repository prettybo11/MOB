package com.example.collegeschedule.utils
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
fun getWeekDateRange(): Pair<String, String> {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ISO_DATE
    var start = if (today.dayOfWeek == DayOfWeek.SUNDAY)
        today.plusDays(1)
    else
        today
    var daysAdded = 0
    var end = start
    while (daysAdded < 5) {
        end = end.plusDays(1)
        if (end.dayOfWeek != DayOfWeek.SUNDAY) {
            daysAdded++
        }
    }
    return start.format(formatter) to end.format(formatter)
}