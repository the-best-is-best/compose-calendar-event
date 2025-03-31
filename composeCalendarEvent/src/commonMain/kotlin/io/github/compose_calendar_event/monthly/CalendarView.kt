package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import io.github.compose_calendar_event.model.ComposeCalendarEvent
import io.github.compose_calendar_event.utils.getDaysOfMonth
import io.github.tcompose_date_picker.TKDatePicker
import io.github.tcompose_date_picker.config.TextFieldType
import io.github.tcompose_date_picker.extensions.toEpochMillis
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarView(
    selectedMonth: LocalDate,
    useAdaptive: Boolean = false,
    isTwoWeeksSupport: Boolean = true,
    selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    events: List<ComposeCalendarEvent>,
    onDateSelected: (LocalDate) -> Unit = {},
    onMonthChanged: (LocalDate) -> Unit = {},
    firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    headerModifier: Modifier = Modifier,
    headerTextStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    selectedDayColor: Color = Color.Blue,
    currentDayColor: Color = Color.Green,
    currentDayTextColor: Color = Color.White,
    eventDayColor: Color = Color.Red,
    displayItem: (@Composable (ComposeCalendarEvent) -> Unit)? = null,
    onEventClick: (ComposeCalendarEvent) -> Unit,
    isDialogOpen: (Boolean) -> Unit,
) {
    var isMonthlyView by remember { mutableStateOf(true) }
    var currentHalf by remember { mutableStateOf(1) }
    var currentMonth by remember { mutableStateOf(selectedMonth) } // Track current month

    val interactionSource = remember { MutableInteractionSource() }
    val daysOfMonth = getDaysOfMonth(currentMonth, firstDayOfWeek)
    val totalWeeks = daysOfMonth.size / 7
    val firstHalfWeeks = totalWeeks / 2
    val splitIndex = firstHalfWeeks * 7
    val splitDays = listOf(
        daysOfMonth.subList(0, splitIndex),
        daysOfMonth.subList(splitIndex, daysOfMonth.size)
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = headerModifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                val newMonth = goToPrev(
                    isMonthlyView = isMonthlyView,
                    selectedMonth = currentMonth,
                    onMonthChanged = { currentMonth = it; onMonthChanged(it) },
                    currentHalf = currentHalf,
                    onChangeHalf = { currentHalf = it }
                )
                if (newMonth != null) currentMonth = newMonth
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous"
                )
            }

            TKDatePicker(
                useAdaptive = useAdaptive,
                textFieldType = TextFieldType.Custom { modifier ->
                    Text(
                        text = "${currentMonth.month.name} ${currentMonth.year}",
                        style = headerTextStyle,
                        modifier = modifier
                    )
                },
                onDateSelected = {
                    val millis = it?.toEpochMillis()
                    if (millis != null) {
                        val newDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                        currentMonth = newDate
                        onMonthChanged(newDate)
                        onDateSelected(newDate)
                    }
                },
                onDismiss = {},
                isDialogOpen = isDialogOpen,
            )

            if (isTwoWeeksSupport) {
                Spacer(Modifier.weight(1f))
                CalendarType(!isMonthlyView) {
                    isMonthlyView = !isMonthlyView
                }
            }

            IconButton(onClick = {
                val newMonth = goToNext(
                    isMonthlyView = isMonthlyView,
                    selectedMonth = currentMonth,
                    onMonthChanged = { currentMonth = it; onMonthChanged(it) },
                    currentHalf = currentHalf,
                    onChangeHalf = { currentHalf = it }
                )
                if (newMonth != null) currentMonth = newMonth
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }

        DayHeaders(firstDayOfWeek)
        Box(
            modifier = Modifier.pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            accumulatedDragAmount > 200 -> goToPrev()
                            accumulatedDragAmount < -200 -> goToNext()
                        }
                        accumulatedDragAmount = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        accumulatedDragAmount += dragAmount
                    }
                )
            }
        ) {
            MonthCalendar(
                days = if (isMonthlyView) daysOfMonth else splitDays[currentHalf - 1],
                events = events,
                onDateSelected = onDateSelected,
                selectedDate = selectedDate,
                selectedDayColor = selectedDayColor,
                currentDayColor = currentDayColor,
                currentDayTextColor = currentDayTextColor,
                eventDayColor = eventDayColor,
                isMonthlyView = isMonthlyView,
                selectedMonth = currentMonth,
                onSelectedMonth = { newMonth ->
                    currentMonth = newMonth
                    onMonthChanged(newMonth)
                },
                currentHalf = currentHalf,
                onChangeCurrentHalf = { currentHalf = it },
                isTwoWeeksSupport = isTwoWeeksSupport,
                onChangeMonthlyView = { isMonthlyView = it }
            )

        }
        Spacer(Modifier.height(30.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val filterEvents = events.filter { it.start.date == selectedDate }
            items(filterEvents.size) { index ->
                val displayEvent = filterEvents[index]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onEventClick(displayEvent) }
                ) {
                    if (displayItem != null) {
                        displayItem(displayEvent)
                    } else {
                        Card(
                            modifier = Modifier.fillParentMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            colors = androidx.compose.material3.CardDefaults.cardColors(
                                containerColor = displayEvent.color
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = displayEvent.name,
                                    color = displayEvent.textColor,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${displayEvent.start.hour}:${displayEvent.start.minute} - ${displayEvent.end.hour}:${displayEvent.end.minute}",
                                    color = displayEvent.textColor.copy(alpha = 0.8f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}