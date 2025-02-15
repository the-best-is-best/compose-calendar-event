package io.github.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.compose_calendar_event.model.ComposeCalendarEvent
import io.github.compose_calendar_event.monthly.CalendarView
import io.github.compose_calendar_event.schedule.ScheduleView
import io.github.compose_calendar_event.weekly.WeeklyCalendar
import io.github.sample.theme.AppTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

enum class CalendarType {
    MONTHLY, ThreeDays, SCHEDULED
}


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val selectedMonth = Clock.System.todayIn(TimeZone.currentSystemDefault())
    var calendarType by remember { mutableStateOf(CalendarType.MONTHLY) } // Track calendar type

    val data = listOf(
        ComposeCalendarEvent(

            "Event #100232",
            color = Color(0xFF1CB0F9),
            textColor = Color.White,
            start = LocalDateTime(2025, 2, 14, 1, 15),
            end = LocalDateTime(2025, 2, 14, 2, 45)
        ),

        ComposeCalendarEvent(
            "Event 1",
            color = Color(0xFF1CB0F9),
            textColor = Color.White,
            start = LocalDateTime(2025, 2, 15, 1, 45),
            end = LocalDateTime(2025, 2, 15, 3, 15)
        ),
        ComposeCalendarEvent(
            "Event 1 2",
            color = Color(0xFF1CB0F9),
            textColor = Color.White,
            start = LocalDateTime(2025, 2, 15, 7, 10),
            end = LocalDateTime(2025, 2, 15, 11, 10)
        ),
        ComposeCalendarEvent(
            "Event 3",
            color = Color(0xFF1CB0F9),
            textColor = Color.White,
            start = LocalDateTime(2025, 3, 26, 3, 45),
            end = LocalDateTime(2025, 3, 26, 7, 15)
        ),
        ComposeCalendarEvent(
            "Event 3 1",
            color = Color(0xFF1CB0F9),
            textColor = Color.White,
            start = LocalDateTime(2025, 3, 28, 3, 45),
            end = LocalDateTime(2025, 3, 28, 7, 15)
        ),
        ComposeCalendarEvent(
            "Event 3 2",
            color = Color(0xFF1CB0F9),
            textColor = Color.White,
            start = LocalDateTime(2025, 3, 21, 3, 45),
            end = LocalDateTime(2025, 3, 21, 7, 15)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Calendar") },
                actions = {
                    var expanded by remember { mutableStateOf(false) } // For dropdown menu

                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Change Calendar Type"
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Monthly View") },
                                onClick = {
                                    calendarType = CalendarType.MONTHLY
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("3 Days View") },
                                onClick = {
                                    calendarType = CalendarType.ThreeDays
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Scheduled View") },
                                onClick = {
                                    calendarType = CalendarType.SCHEDULED
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (calendarType) {
                CalendarType.MONTHLY -> MyMonthlyCalendar(data, selectedMonth)
                CalendarType.ThreeDays -> MyWeeklyView(data, selectedMonth)
                CalendarType.SCHEDULED -> MyScheduledCalendar(data)
            }
        }
    }
}


@Composable
fun MyMonthlyCalendar(
    data: List<ComposeCalendarEvent>,
    selectedMonth: LocalDate,
) {
    var _selectedMonth by remember { mutableStateOf(selectedMonth) }
    CalendarView(
        isTwoWeeksSupport = true,
        selectedDate = _selectedMonth,
        currentDayTextColor = Color.White,
        currentDayColor = Color.Blue,

        eventDays = data.map { it.start.date },
        onMonthChanged = { newMonth ->
            _selectedMonth = newMonth
        },
        onDateSelected = { newDate ->
            _selectedMonth = newDate
        },

        firstDayOfWeek = DayOfWeek.MONDAY,

        displayItem = { date ->
            Column {
                data.filter { it.start.date == date }.forEach { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = event.color)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = event.name,
                                color = event.textColor,
                                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${event.start.hour}:${event.start.minute} - ${event.end.hour}:${event.end.minute}",
                                color = event.textColor.copy(alpha = 0.8f),
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

    )

}

@Composable
fun MyWeeklyView(
    data: List<ComposeCalendarEvent>,
    selectedMonth: LocalDate
) {
    var _selectedMonth by remember { mutableStateOf(selectedMonth) }

    WeeklyCalendar(
        events = data,
        currentDate = _selectedMonth,
        currentDayTextColor = Color.White,
        currentDayColor = Color.Blue,
        onDateSelected = {
            _selectedMonth = it
        }
    )
}

@Composable
fun MyScheduledCalendar(
    data: List<ComposeCalendarEvent>
) {
    ScheduleView(events = data)

}