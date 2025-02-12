package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.github.compose_calendar_event.utils.getDaysOfMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn


@Composable
fun <T> CalendarView(
    isTwoWeeksSupport: Boolean = true,
    selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    eventDays: List<LocalDate> = emptyList(),
    onDateSelected: (LocalDate) -> Unit = {},
    onMonthChanged: (LocalDate) -> Unit = {},
    firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    headerModifier: Modifier = Modifier,
    headerTextStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    selectedDayColor: Color = Color.Blue,
    currentDayColor: Color = Color.Green,
    eventDayColor: Color = Color.Red,
    displayItem: @Composable (LocalDate) -> Unit
) {
    var selectedMonth by remember {
        mutableStateOf(
            LocalDate(
                selectedDate.year,
                selectedDate.month,
                1
            )
        )
    }
    var isMonthlyView by remember { mutableStateOf(true) }
    var currentHalf by remember { mutableStateOf(1) }
    val daysOfMonth = getDaysOfMonth(selectedMonth, firstDayOfWeek)
    val totalWeeks = daysOfMonth.size / 7
    val firstHalfWeeks = totalWeeks / 2
    val splitIndex = firstHalfWeeks * 7
    val splitDays = listOf(
        daysOfMonth.subList(0, splitIndex),
        daysOfMonth.subList(splitIndex, daysOfMonth.size)
    )


    Column(
        modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(16.dp)
        .pointerInput(Unit) {
            if (isTwoWeeksSupport) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (isTwoWeeksSupport) {
                        isMonthlyView = dragAmount >= 0
                        currentHalf = 1
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
                        isMonthlyView = available.y >= 0
                        currentHalf = 1
                    }
                    return Offset.Zero
                }
            }

        })
    ) {


        Row(
            modifier = headerModifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (isMonthlyView) {
                    selectedMonth = selectedMonth.minus(DatePeriod(months = 1))
                    onMonthChanged(selectedMonth)
                } else {
                    if (currentHalf == 2) {
                        currentHalf = 1

                    } else {
                        currentHalf = 2
                        selectedMonth = selectedMonth.minus(DatePeriod(months = 1))
                        onMonthChanged(selectedMonth)
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous"
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${selectedMonth.month.name} ${selectedMonth.year}",
                    style = headerTextStyle,
                    modifier = Modifier.clickable {
                        onDateSelected(selectedMonth)

                    }
                        .padding(bottom = 8.dp)
                )

            }

            if (isTwoWeeksSupport) {
                Spacer(Modifier.weight(1f))

                CalendarType(!isMonthlyView) {
                    isMonthlyView = !isMonthlyView
                }
            }
            IconButton(onClick = {
                if (isMonthlyView) {
                    selectedMonth = selectedMonth.plus(DatePeriod(months = 1))
                    onMonthChanged(selectedMonth)
                } else {
                    if (currentHalf == 1) {
                        currentHalf = 2
                    } else {
                        currentHalf = 1
                        selectedMonth = selectedMonth.plus(DatePeriod(months = 1))
                        onMonthChanged(selectedMonth)
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }

        DayHeaders(firstDayOfWeek)
        MonthCalendar(
            days = if (isMonthlyView) daysOfMonth else splitDays[currentHalf - 1],
            eventDays = eventDays,
            onDateSelected = onDateSelected,
            selectedDate = selectedDate,
            selectedDayColor = selectedDayColor,
            currentDayColor = currentDayColor,
            eventDayColor = eventDayColor,
        )
        Spacer(Modifier.height(30.dp))

        LazyColumn {
            item {
                displayItem(selectedDate)

            }
        }

    }
}
