package io.github.compose_calendar_event.monthly

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

// Update goToPrev and goToNext to return the new month
fun goToPrev(
    isMonthlyView: Boolean,
    selectedMonth: LocalDate,
    onMonthChanged: (LocalDate) -> Unit,
    currentHalf: Int,
    onChangeHalf: (Int) -> Unit
): LocalDate? {
    return if (isMonthlyView) {
        val newMonth = selectedMonth.minus(DatePeriod(months = 1))
        onMonthChanged(newMonth)
        newMonth
    } else {
        if (currentHalf == 2) {
            onChangeHalf(1)
            null
        } else {
            onChangeHalf(2)
            val newMonth = selectedMonth.minus(DatePeriod(months = 1))
            onMonthChanged(newMonth)
            newMonth
        }
    }
}

fun goToNext(
    isMonthlyView: Boolean,
    selectedMonth: LocalDate,
    onMonthChanged: (LocalDate) -> Unit,
    currentHalf: Int,
    onChangeHalf: (Int) -> Unit
): LocalDate? {
    return if (isMonthlyView) {
        val newMonth = selectedMonth.plus(DatePeriod(months = 1))
        onMonthChanged(newMonth)
        newMonth
    } else {
        if (currentHalf == 1) {
            onChangeHalf(2)
            null
        } else {
            onChangeHalf(1)
            val newMonth = selectedMonth.plus(DatePeriod(months = 1))
            onMonthChanged(newMonth)
            newMonth
        }
    }
}