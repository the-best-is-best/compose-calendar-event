package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val interactionSource = remember { mutableStateOf(MutableInteractionSource()) }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .background(
                color = when {
                    isSelected -> selectedDayColor
                    isCurrentDay -> currentDayColor
                    else -> Color.Transparent
                },
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource.value,
                indication = null
            ) {
                if (date != 0) onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (date == 0) "" else "$date",
                color = if (isSelected || isCurrentDay) currentDayTextColor else Color.Black,
                fontSize = 14.sp,
                fontWeight = if (isSelected || isCurrentDay) FontWeight.Bold else FontWeight.Normal
            )

            if (hasEvent) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            color = eventDayColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}