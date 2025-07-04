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
import io.github.tcompose_date_picker.extensions.now
import io.github.tcompose_date_picker.extensions.toEpochMillis
import io.github.tcompose_date_picker.extensions.toLocalDate
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun CalendarView(
    useAdaptive: Boolean = false,
    isTwoWeeksSupport: Boolean = true,
    selectedDate: LocalDate = LocalDate.now(),
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

    var selectedMonth by remember {
        mutableStateOf(LocalDate(selectedDate.year, selectedDate.month, 1))
    }
    var isMonthlyView by remember { mutableStateOf(true) }
    var currentHalf by remember { mutableStateOf(1) }

    val interactionSource = remember { MutableInteractionSource() }

    val daysOfMonth = getDaysOfMonth(selectedMonth, firstDayOfWeek)
    val totalWeeks = daysOfMonth.size / 7
    val firstHalfWeeks = totalWeeks / 2
    val splitIndex = firstHalfWeeks * 7
    val splitDays = listOf(
        daysOfMonth.subList(0, splitIndex),
        daysOfMonth.subList(splitIndex, daysOfMonth.size)
    )


    fun goToPrev(

    ) {
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
    }

    fun goToNext(
    ) {
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
    }

    var accumulatedDragAmount by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)

    ) {
        Row(
            modifier = headerModifier.fillMaxWidth(),
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

            TKDatePicker(
                useAdaptive = useAdaptive,
                textFieldType = TextFieldType.Custom { modifier ->
                    Text(
                        text = "${selectedMonth.month.name} ${selectedMonth.year}",
                        style = headerTextStyle,
                        modifier = modifier


                    )
                },
                onDateSelected = {
                    val millis = it?.toEpochMillis()
                    if (millis != null) {
                        val newDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDate(TimeZone.currentSystemDefault())
                        selectedMonth = LocalDate(newDate.year, newDate.month, 1)
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
                goToNext()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }



        DayHeaders(firstDayOfWeek)
        MonthCalendar(
            modifier = Modifier.pointerInput(Unit) {
                if (isTwoWeeksSupport) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (isTwoWeeksSupport) {
                            isMonthlyView = dragAmount >= 20
                            currentHalf = 1
                        }
                    }
                }
            }
                .pointerInput(Unit) {
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
                .nestedScroll(remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {

                            if (isTwoWeeksSupport) {
                                if (available.y > 20) {
                                    isMonthlyView = true
                                    currentHalf = 1
                                } else if (available.y < -20) {
                                    isMonthlyView = false
                                    currentHalf = 1

                                }

                            }
                            return Offset.Zero
                        }
                    }
                }),
            days = if (isMonthlyView) daysOfMonth else splitDays[currentHalf - 1],
            events = events,
            onDateSelected = onDateSelected,
            selectedDate = selectedDate,
            selectedDayColor = selectedDayColor,
            currentDayColor = currentDayColor,
            currentDayTextColor = currentDayTextColor,
            eventDayColor = eventDayColor,
        )

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
                        ) { onEventClick(displayEvent) } // Click outside displayItem will work
                ) {
                    if (displayItem != null) {
                        displayItem(displayEvent) // Custom UI for the event
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
