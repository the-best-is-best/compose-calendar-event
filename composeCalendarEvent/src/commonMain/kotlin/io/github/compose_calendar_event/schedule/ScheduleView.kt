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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.compose_calendar_event.model.ComposeCalendarEvent
import kotlinx.datetime.Month

@Composable
fun ScheduleView(
    events: List<ComposeCalendarEvent>,
    headerModifier: Modifier = Modifier.padding(vertical = 8.dp),
    headerTextStyle: TextStyle? = null,
    dayOfWeekModifier: Modifier = Modifier.fillMaxWidth()
        .background(Color.LightGray)
        .padding(8.dp),
    dayOfWeekTextStyle: TextStyle? = null,
    onEventClick: (ComposeCalendarEvent) -> Unit,
    displayItem: (@Composable (ComposeCalendarEvent) -> Unit)? = null

) {
    val groupedEvents = events.groupBy { it.start.date } // Group by date

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        var lastMonth: Month? = null

        groupedEvents.forEach { (date, dayEvents) ->
            // Show month name when it changes
            if (date.month != lastMonth) {
                item {
                    Text(
                        text = "${date.month.name} ${date.year}",
                        style = headerTextStyle ?: TextStyle(
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold
                        ),

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
                        ) { onEventClick(event) } // Click outside displayItem will work
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
