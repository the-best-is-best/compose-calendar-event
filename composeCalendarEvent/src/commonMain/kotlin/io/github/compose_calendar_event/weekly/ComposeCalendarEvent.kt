package io.github.compose_calendar_event.weekly

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDateTime

data class ComposeCalendarEvent(
    val name: String,
    val color: Color = Color.Green,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val description: String? = null,
)
