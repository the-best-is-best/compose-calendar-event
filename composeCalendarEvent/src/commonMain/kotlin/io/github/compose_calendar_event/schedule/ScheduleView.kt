package io.github.compose_calendar_event.schedule


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.compose_calendar_event.model.ComposeCalendarEvent
import io.github.tcompose_date_picker.TKDatePicker
import io.github.tcompose_date_picker.config.TextFieldType
import io.github.tcompose_date_picker.extensions.toEpochMillis
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal annotation class ExperimentalScheduleView

@OptIn(ExperimentalMaterial3Api::class)

@ExperimentalScheduleView
@Composable
fun ScheduleView(
    useAdaptive: Boolean = false,
    isAutoScrollEnabled: Boolean = true,
    events: List<ComposeCalendarEvent>,
    headerModifier: Modifier = Modifier.padding(vertical = 8.dp),
    headerTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    dayOfWeekModifier: Modifier = Modifier
        .fillMaxWidth()
        .background(Color.LightGray)
        .padding(8.dp),
    dayOfWeekTextStyle: TextStyle? = null,
    onEventClick: (ComposeCalendarEvent) -> Unit,
    displayItem: (@Composable (ComposeCalendarEvent) -> Unit)? = null,
    isDialogOpen: (Boolean) -> Unit,
) {
    val groupedEvents = events.groupBy { it.start.date }
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // تعيين selectedDate إلى التاريخ الحالي
    var selectedDate by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )
    }

    // التمرير إلى أقرب تاريخ عند بدء التشغيل
    LaunchedEffect(Unit) {
        val availableDates = groupedEvents.keys.sorted()
        var index = availableDates.indexOfFirst { it == selectedDate }

        if (index == -1) {
            index = availableDates.indexOfFirst { it >= selectedDate }
        }

        if (index == -1) {
            index = availableDates.indexOfLast { it < selectedDate }
        }

        if (index >= 0) {
            lazyListState.scrollToItem(index)
        }
    }

    Column {
        if (isAutoScrollEnabled) {
            TKDatePicker(
                useAdaptive = useAdaptive,
                textFieldType = TextFieldType.Custom { modifier ->
                    Text(
                        text = "${selectedDate.month.name} ${selectedDate.year}",
                        style = headerTextStyle,
                        modifier = modifier
                    )
                },
                onDateSelected = { epochMillis ->
                    if (epochMillis != null) {
                        val instant = Instant.fromEpochMilliseconds(epochMillis.toEpochMillis())
                        selectedDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

                        val availableDates = groupedEvents.keys.sorted()
                        var index = availableDates.indexOfFirst { it == selectedDate }

                        if (index == -1) {
                            index = availableDates.indexOfFirst { it > selectedDate }
                        }

                        if (index == -1) {
                            index = availableDates.indexOfLast { it < selectedDate }
                        }

                        if (index != -1) {
                            val previousDaysCount =
                                availableDates.take(index).sumOf { groupedEvents[it]?.size ?: 0 }
                            val offset = index * 2 // عنصران إضافيان لكل يوم (الشهر + اليوم)
                            val finalIndex = previousDaysCount + offset

                            scope.launch {
                                lazyListState.animateScrollToItem(finalIndex)
                            }
                        }
                    }
                },
                onDismiss = {},
                isDialogOpen = isDialogOpen
            )
        }

        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            var lastMonth: Month? = null

            groupedEvents.forEach { (date, dayEvents) ->
                if (date.month != lastMonth) {
                    item {
                        Text(
                            text = "${date.month.name} ${date.year}",
                            style = headerTextStyle,
                            modifier = headerModifier
                        )
                    }
                    lastMonth = date.month
                }

                item {
                    Text(
                        text = "${date.dayOfWeek.name}, $date",
                        style = dayOfWeekTextStyle ?: TextStyle(fontWeight = FontWeight.Bold),
                        modifier = dayOfWeekModifier
                    )
                }

                items(dayEvents) { event ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onEventClick(event) }
                    ) {
                        if (displayItem != null) {
                            displayItem(event)
                        } else {
                            EventItem(event)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun EventItem(event: ComposeCalendarEvent) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = event.color)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = event.name, fontWeight = FontWeight.Bold, color = event.textColor)
            Text(
                text = "🕒 ${event.start.hour}:${event.start.minute} - ${event.end.hour}:${event.end.minute}",
                color = event.textColor
            )
            event.description?.let {
                Text(text = "📄 $it", color = event.textColor)
            }
        }
    }
}
