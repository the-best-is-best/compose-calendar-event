# Compose Calendar Event

## 📌 Overview

Compose Calendar Event is a **Compose Multiplatform** package that provides flexible calendar views to display events.
It supports all platforms (Android, iOS, Desktop, Web) using only `kotlinx.datetime` for date handling.

<div align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href="https://android-arsenal.com/api?level=21" rel="nofollow">
    <img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" style="max-width: 100%;">
</a>
<img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
  <img src="https://img.shields.io/badge/Platform-iOS%20%2F%20macOS-lightgrey.svg?logo=apple" alt="Badge iOS" />
  <img src="https://img.shields.io/badge/Platform-JVM-8A2BE2.svg?logo=openjdk" alt="Badge JVM" />
    <img src="https://img.shields.io/badge/Platform-WASM%20%2F%20JS-yellow.svg?logo=javascript" alt="Badge JS" />
<a href="https://github.com/the-best-is-best/"><img alt="Profile" src="https://img.shields.io/badge/github-%23181717.svg?&style=for-the-badge&logo=github&logoColor=white" height="20"/></a>

</div>

## 🚀 Features

- 🗓 **Multiple Views**: Monthly, Bi-Weekly, 3 Days, and Schedule.
- 🌍 **Cross-Platform**: Works on Android, iOS, Desktop, and Web.
- 📅 **Date Handling**: Uses `kotlinx.datetime`.
- 🎨 **Customizable**: Allows styling and event customization.

## 📦 Installation

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/compose-calendar-event)](https://central.sonatype.com/artifact/io.github.the-best-is-best/compose-calendar-event)

Compose Calendar Event is available on `mavenCentral()`.

## 🛠 Usage

```kotlin
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
```

## 📸 Screenshots

### Monthly

![Monthly View](https://raw.githubusercontent.com/the-best-is-best/compose-calendar-event/refs/heads/main/images/img.png)

### 2 Weeks

![2weeks View](https://raw.githubusercontent.com/the-best-is-best/compose-calendar-event/refs/heads/main/images/img_1.png)

### 3 Days

![3 Days View](https://raw.githubusercontent.com/the-best-is-best/compose-calendar-event/refs/heads/main/images/img_2.png)
![3 Days View2](https://raw.githubusercontent.com/the-best-is-best/compose-calendar-event/refs/heads/main/images/img_3.png)

### Scheduled

![Schedule View](https://raw.githubusercontent.com/the-best-is-best/compose-calendar-event/refs/heads/main/images/img_4.png)

## 📝 Contribution

Contributions are welcome! Feel free to open an **Issue** or submit a **Pull Request**.

