package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.datetime.DayOfWeek


@Composable
internal fun DayHeaders(firstDayOfWeek: DayOfWeek) {
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
