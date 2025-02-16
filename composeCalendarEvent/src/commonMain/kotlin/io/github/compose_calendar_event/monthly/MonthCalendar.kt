package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.compose_calendar_event.model.ComposeCalendarEvent
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn


@Composable
internal fun MonthCalendar(
    days: List<Int>,
    events: List<ComposeCalendarEvent>,
    onDateSelected: (LocalDate) -> Unit,
    selectedDate: LocalDate,
    selectedDayColor: Color,
    currentDayColor: Color,

    currentDayTextColor: Color,
    eventDayColor: Color
) {
    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.padding(top = 8.dp)) {
        items(days) { date ->
            val safeDate =
                runCatching { LocalDate(selectedDate.year, selectedDate.month, date) }.getOrNull()
            val hasEvent = safeDate != null && events.any { event ->
                event.start.date == safeDate
            }
            DayItem(
                date = date,
                isSelected = safeDate == selectedDate,
                isCurrentDay = safeDate == Clock.System.todayIn(TimeZone.currentSystemDefault()),
                hasEvent = hasEvent,
                selectedDayColor = selectedDayColor,
                currentDayColor = currentDayColor,
                currentDayTextColor = currentDayTextColor,
                eventDayColor = eventDayColor
            ) { if (safeDate != null) onDateSelected(safeDate) }
        }
    }
}
