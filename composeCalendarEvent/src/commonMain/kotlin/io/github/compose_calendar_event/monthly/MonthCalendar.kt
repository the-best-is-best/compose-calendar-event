package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn


@Composable
fun MonthCalendar(
    days: List<Int>,
    eventDays: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    selectedDate: LocalDate,
    selectedDayColor: Color,
    currentDayColor: Color,
    eventDayColor: Color
) {
    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.padding(top = 8.dp)) {
        items(days) { date ->
            val safeDate =
                runCatching { LocalDate(selectedDate.year, selectedDate.month, date) }.getOrNull()
            val hasEvent = safeDate != null && eventDays.contains(safeDate)
            DayItem(
                date = date,
                isSelected = safeDate == selectedDate,
                isCurrentDay = safeDate == Clock.System.todayIn(TimeZone.currentSystemDefault()),
                hasEvent = hasEvent,
                selectedDayColor = selectedDayColor,
                currentDayColor = currentDayColor,
                eventDayColor = eventDayColor
            ) { if (safeDate != null) onDateSelected(safeDate) }
        }
    }
}
