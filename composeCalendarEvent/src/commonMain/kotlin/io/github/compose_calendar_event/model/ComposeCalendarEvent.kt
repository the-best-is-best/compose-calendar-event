package io.github.compose_calendar_event.model

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDateTime

data class ComposeCalendarEvent(
    val id: Any,
    val name: String,
    val color: Color = Color.Green,
    val textColor: Color = Color.Black,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val description: String? = null,
)
