package io.github.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.compose_calendar_event.CalendarView
import io.github.sample.theme.AppTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
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


    CalendarView(
        selectedDate = selectedMonth,
        eventDays = listOf(
            LocalDate(2025, 2, 14),
            LocalDate(2025, 2, 20),
            LocalDate(2025, 3, 28)
        ),
        onMonthChanged = { newMonth ->
            selectedMonth = newMonth
        },
        onDateSelected = { newDate ->
            selectedMonth = newDate
        },

        firstDayOfWeek = DayOfWeek.MONDAY
    )
}
