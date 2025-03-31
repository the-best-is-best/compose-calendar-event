package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
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
    eventDayColor: Color,
    isMonthlyView: Boolean = true,
    selectedMonth: LocalDate,
    onSelectedMonth: (LocalDate) -> Unit,
    currentHalf: Int,
    onChangeCurrentHalf: (Int) -> Unit,
    isTwoWeeksSupport: Boolean,
    onChangeMonthlyView: (Boolean) -> Unit,
) {
    var accumulatedDragAmount by remember { mutableStateOf(0f) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.padding(top = 8.dp)
            .pointerInput(Unit) {
                if (isTwoWeeksSupport) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (isTwoWeeksSupport) {
                            onChangeMonthlyView(dragAmount >= 20)
                            onChangeCurrentHalf(1)
                        }
                    }
                }
            }

            .nestedScroll(remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        if (isTwoWeeksSupport) {
                            if (available.y > 20) {
                                onChangeMonthlyView(true)
                                onChangeCurrentHalf(1)
                            } else if (available.y < -20) {
                                onChangeMonthlyView(false)
                                onChangeCurrentHalf(1)
                            }
                        }
                        return Offset.Zero
                    }
                }
            })
    ) {
        items(days) { date ->
            val safeDate = runCatching { LocalDate(selectedDate.year, selectedDate.month, date) }.getOrNull()
            val hasEvent = safeDate != null && events.any { event -> event.start.date == safeDate }
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
