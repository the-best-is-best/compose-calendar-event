package io.github.compose_calendar_event.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal fun LocalDate.get3Days(): List<LocalDate> {
    return List(3) { this.plus(DatePeriod(days = it)) }
}