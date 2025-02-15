package io.github.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.compose_calendar_event.monthly.CalendarView
import io.github.compose_calendar_event.weekly.ComposeCalendarEvent
import io.github.sample.theme.AppTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn


@Composable
internal fun App() = AppTheme {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarScreen()
    }
}

@Composable
fun CalendarScreen() {
    var selectedMonth by remember { mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault())) }

    val data = listOf(
        ComposeCalendarEvent(
            "Event #100232",
            start = LocalDateTime(2025, 2, 14, 1, 15),
            end = LocalDateTime(2025, 2, 14, 2, 45)
        ),

        ComposeCalendarEvent(
            "Event 1",
            start = LocalDateTime(2025, 2, 15, 1, 45),
            end = LocalDateTime(2025, 2, 15, 3, 15)
        ),
        ComposeCalendarEvent(
            "Event 1 2",
            start = LocalDateTime(2025, 2, 15, 7, 10),
            end = LocalDateTime(2025, 2, 15, 11, 10)
        ),
        ComposeCalendarEvent(
            "Event 3",
            start = LocalDateTime(2025, 3, 28, 3, 45),
            end = LocalDateTime(2025, 3, 28, 7, 15)
        )

    )
    CalendarView(
        isTwoWeeksSupport = true,
        selectedDate = selectedMonth,
        eventDays = data.map { it.start.date },
        onMonthChanged = { newMonth ->
            selectedMonth = newMonth
        },
        onDateSelected = { newDate ->
            selectedMonth = newDate
        },

        firstDayOfWeek = DayOfWeek.MONDAY,

        displayItem = { date ->
            for (event in data) {
                if (event.start.date == date) {
                    Text(text = event.name)
                }
            }
        }
    )
//    WeeklyCalendar(
//        events = data,
//        currentDate = selectedMonth,
//        onDateSelected = {
//            selectedMonth = it
//        }
//    )
}
