package io.github.compose_calendar_event.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus


internal fun getDaysOfMonth(date: LocalDate, firstDayOfWeek: DayOfWeek): List<Int> {
    val firstDayOfMonth = LocalDate(date.year, date.month, 1)
    val lastDay =
        firstDayOfMonth.plus(DatePeriod(months = 1)).minus(DatePeriod(days = 1)).dayOfMonth

    val startDayOffset = (firstDayOfMonth.dayOfWeek.ordinal - firstDayOfWeek.ordinal + 7) % 7
    val days = mutableListOf<Int>().apply {
        addAll(List(startDayOffset) { 0 })
        addAll(1..lastDay)
        while (size % 7 != 0) add(0)
    }
    return days
}