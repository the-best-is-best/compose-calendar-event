package io.github.compose_calendar_event.weekly

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import io.github.compose_calendar_event.utils.get3Days
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlin.time.Duration

@Composable
fun WeeklyCalendar(
    events: List<ComposeCalendarEvent>,
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var visibleStartDate by remember { mutableStateOf(currentDate) }
    val daysOfWeek = remember(visibleStartDate) { visibleStartDate.get3Days() }
    val hours = (0..23).toList()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                visibleStartDate = visibleStartDate.minus(DatePeriod(days = 3))
                onDateSelected(visibleStartDate)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous"
                )
            }
            Text(
                text = "${visibleStartDate.month.name} ${visibleStartDate.year}",
                style = TextStyle(fontSize = 18.sp, color = Color.Black)
            )
            IconButton(onClick = {
                visibleStartDate = visibleStartDate.plus(DatePeriod(days = 3))
                onDateSelected(visibleStartDate)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.width(60.dp).verticalScroll(scrollState).offset(y = 40.dp)) {
                hours.forEach { hour ->
                    Box(modifier = Modifier.height(60.dp).padding(horizontal = 4.dp)) {
                        Text(text = "$hour:00", style = TextStyle(fontSize = 12.sp))
                    }
                }
            }
            LazyRow(modifier = Modifier.weight(1f)) {
                items(daysOfWeek) { day ->
                    Column(
                        modifier = Modifier.fillParentMaxWidth(.35f).padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = day.dayOfWeek.name.take(3), style = TextStyle(fontSize = 10.sp))
                        Text(text = "${day.dayOfMonth}")
                        Box(
                            modifier = Modifier.fillMaxHeight().background(Color.LightGray)
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
fun EventView(event: ComposeCalendarEvent) {
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


