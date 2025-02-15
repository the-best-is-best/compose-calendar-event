package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
internal fun DayItem(
    date: Int,
    isSelected: Boolean,
    isCurrentDay: Boolean,
    hasEvent: Boolean,
    selectedDayColor: Color,
    currentDayColor: Color,
    currentDayTextColor: Color,

    eventDayColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { if (date != 0) onClick() }
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
                text = if (date == 0) "" else "$date",
                color = if (isSelected) currentDayTextColor else if (isCurrentDay) currentDayTextColor else Color.Black
            )
            if (hasEvent) Box(modifier = Modifier.size(5.dp).background(eventDayColor, CircleShape))
        }
    }
}