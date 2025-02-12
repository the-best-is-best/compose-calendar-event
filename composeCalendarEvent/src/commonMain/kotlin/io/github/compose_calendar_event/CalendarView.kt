package io.github.compose_calendar_event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

@Composable
fun CalendarView(
    selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    eventDays: List<LocalDate> = emptyList(),
    onDateSelected: (LocalDate) -> Unit = {},
    onMonthChanged: (LocalDate) -> Unit = {},
    firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    headerModifier: Modifier = Modifier,
    headerTextStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium,
    selectedDayColor: Color = Color.Blue,
    currentDayColor: Color = Color.Green,
    eventDayColor: Color = Color.Red,
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
    val splitDays = daysOfMonth.chunked(daysOfMonth.size / 2 + daysOfMonth.size % 2)

    Column(modifier = Modifier.padding(16.dp).pointerInput(Unit) {
        detectVerticalDragGestures { _, dragAmount ->
            isMonthlyView = dragAmount >= 0
            currentHalf = 1
        }
    }) {
        Row(
            modifier = headerModifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (isMonthlyView) {
                    selectedMonth = selectedMonth.minus(DatePeriod(months = 1))
                } else {
                    if (currentHalf == 2) {
                        currentHalf = 1
                    } else {
                        selectedMonth = selectedMonth.minus(DatePeriod(months = 1))
                        currentHalf = 2
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous"
                )
            }
            Text(
                text = "${selectedMonth.month.name} ${selectedMonth.year}",
                style = headerTextStyle,
                modifier = Modifier
                    .clickable { onDateSelected(selectedMonth) }
                    .padding(bottom = 8.dp)
            )
            IconButton(onClick = {
                if (isMonthlyView) {
                    selectedMonth = selectedMonth.plus(DatePeriod(months = 1))
                } else {
                    if (currentHalf == 1) {
                        currentHalf = 2
                    } else {
                        selectedMonth = selectedMonth.plus(DatePeriod(months = 1))
                        currentHalf = 1
                    }
                }
                onMonthChanged(selectedMonth)
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
            eventDayColor = eventDayColor
        )
    }
}

fun getDaysOfMonth(date: LocalDate, firstDayOfWeek: DayOfWeek): List<Int> {
    val firstDayOfMonth = LocalDate(date.year, date.month, 1)
    val lastDay =
        firstDayOfMonth.plus(DatePeriod(months = 1)).minus(DatePeriod(days = 1)).dayOfMonth

    // حساب الفرق بين أول يوم في الشهر وأول يوم مطلوب في الأسبوع
    val startDayOffset = (firstDayOfMonth.dayOfWeek.ordinal - firstDayOfWeek.ordinal + 7) % 7

    val days = mutableListOf<Int>()

    // إضافة الأيام الفارغة قبل بداية الشهر بالقيمة 0
    repeat(startDayOffset) { days.add(0) }

    // إضافة الأيام الفعلية للشهر كأعداد صحيحة
    days.addAll((1..lastDay))

    return days
}


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
            val hasEvent = if (date != 0) eventDays.contains(
                LocalDate(
                    selectedDate.year,
                    selectedDate.month,
                    date
                )
            ) else false
            DayItem(
                date = date,
                selectedDate = selectedDate,
                isSelected = if (date != 0) LocalDate(
                    selectedDate.year,
                    selectedDate.month,
                    date
                ) == selectedDate else false,
                isCurrentDay = if (date != 0) LocalDate(
                    selectedDate.year,
                    selectedDate.month,
                    date
                ) == Clock.System.todayIn(TimeZone.currentSystemDefault()) else false,
                hasEvent = hasEvent,
                selectedDayColor = selectedDayColor,
                currentDayColor = currentDayColor,
                eventDayColor = eventDayColor
            ) { onDateSelected(LocalDate(selectedDate.year, selectedDate.month, date)) }
        }
    }
}


@Composable
fun DayHeaders(firstDayOfWeek: DayOfWeek) {
    val days =
        DayOfWeek.entries.drop(firstDayOfWeek.ordinal) + DayOfWeek.entries.take(firstDayOfWeek.ordinal)
    Row(Modifier.fillMaxWidth()) {
        days.forEach { day ->
            Text(
                text = day.name.take(3),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DayItem(
    selectedDate: LocalDate,
    date: Int,
    isSelected: Boolean,
    isCurrentDay: Boolean,
    hasEvent: Boolean,
    selectedDayColor: Color,
    currentDayColor: Color,
    eventDayColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable { if (date != 0) onClick() }
            .background(
                when {
                    isSelected -> selectedDayColor
                    isCurrentDay -> currentDayColor
                    else -> Color.Transparent
                },
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (date == 0) "" else "${
                    LocalDate(
                        selectedDate.year,
                        selectedDate.month,
                        date
                    ).dayOfMonth
                }", color = if (isSelected || isCurrentDay) Color.White else Color.Black
            )
            if (hasEvent) Box(modifier = Modifier.size(5.dp).background(eventDayColor, CircleShape))
        }
    }
}
