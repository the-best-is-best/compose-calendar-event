package io.github.compose_calendar_event.weekly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import io.github.compose_calendar_event.utils.get3Days
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyCalendar(
    events: List<ComposeCalendarEvent>,
    currentDate: LocalDate,
    headerModifier: Modifier = Modifier,
    headerTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    currentDayColor: Color = Color.Green,
    currentDayTextColor: Color = Color.White,
    onDateSelected: (LocalDate) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var accumulatedDragAmount by remember { mutableStateOf(0f) }
    var visibleStartDate by remember { mutableStateOf(currentDate) }
    val daysOfWeek = remember(visibleStartDate) { visibleStartDate.get3Days() }
    val hours = (0..23).toList()

    val scrollState = rememberScrollState()

    fun goToPrev() {
        visibleStartDate = visibleStartDate.minus(DatePeriod(days = 3))
        onDateSelected(visibleStartDate)
    }

    fun goToNext() {
        visibleStartDate = visibleStartDate.plus(DatePeriod(days = 3))
        onDateSelected(visibleStartDate)
    }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val newDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                        visibleStartDate = newDate

                        onDateSelected(newDate)

                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                goToPrev()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous"
                )
            }
            Text(
                modifier = headerModifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { showDatePicker = true },
                text = "${visibleStartDate.month.name} ${visibleStartDate.year}",
                style = headerTextStyle
            )
            IconButton(onClick = {
                goToNext()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.padding(top = 50.dp)
            ) {
                Column(
                    modifier = Modifier.width(60.dp).verticalScroll(scrollState)

                ) {
                    hours.forEach { hour ->
                        Box(modifier = Modifier.height(60.dp).padding(horizontal = 4.dp)) {
                            if (hour != 0)
                                Text(text = "$hour:00", style = TextStyle(fontSize = 12.sp))
                        }
                    }
                }
            }
            LazyRow(modifier = Modifier.weight(1f).pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    accumulatedDragAmount += dragAmount

                    if (accumulatedDragAmount > 200) {
                        goToPrev()
                        accumulatedDragAmount = 0f
                    } else if (accumulatedDragAmount < -200) {
                        goToNext()
                        accumulatedDragAmount = 0f
                    }
                }
            }, userScrollEnabled = false) {
                items(daysOfWeek) { day ->
                    val isCurrent = day == today

                    Column(
                        modifier = Modifier.fillParentMaxWidth(.35f).padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier.fillMaxWidth(.5f).height(50.dp)
                                .clip(CircleShape)
                                .background(if (isCurrent) currentDayColor else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = day.dayOfWeek.name.take(3),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = if (isCurrent) currentDayTextColor else Color.Black
                                    )
                                )
                                Text(
                                    text = "${day.dayOfMonth}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = if (isCurrent) currentDayTextColor else Color.Black
                                    )
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.padding(top = 4.dp).fillMaxHeight()
                                .background(Color.LightGray)
                                .verticalScroll(scrollState)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().drawBehind {
                                val hourHeight = 60.dp.toPx()
                                val lineColor = Color.Gray
                                val strokeWidth = 1.dp.toPx()
                                for (i in 1 until 24) {
                                    val y = i * hourHeight
                                    drawLine(
                                        color = lineColor,
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = strokeWidth
                                    )
                                }
                            }) {
                                Column {
                                    hours.forEach { _ ->
                                        Box(modifier = Modifier.height(60.dp).fillMaxWidth()) {}
                                    }
                                }
                            }
                            events.filter { it.start.date == day }
                                .forEach { event -> EventView(event) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventView(event: ComposeCalendarEvent) {
    val eventOffset = calculateEventOffset(event.start)
    val eventHeight = calculateEventHeight(event.start, event.end)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = eventOffset)
            .height(eventHeight)
            .background(event.color)
    ) {

        Text(
            modifier = Modifier.padding(10.dp),
            text = event.name,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp
        )
    }
}

private fun calculateEventHeight(startTime: LocalDateTime, endTime: LocalDateTime): Dp {
    val durationMinutes = calculateDuration(startTime, endTime).inWholeMinutes.toFloat()
    return durationMinutes.dp
}

private fun calculateEventOffset(startTime: LocalDateTime): Dp {
    val totalMinutes = startTime.hour * 60 + startTime.minute
    val hourHeight = 60.dp
    val minuteHeight = hourHeight / 60
    return totalMinutes * minuteHeight
}

private fun calculateDuration(startTime: LocalDateTime, endTime: LocalDateTime): Duration {
    val startInstant = startTime.toInstant(TimeZone.UTC)
    val endInstant = endTime.toInstant(TimeZone.UTC)
    return endInstant - startInstant
}


