package io.github.compose_calendar_event.monthly.calendar_tab_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import io.github.compose_calendar_event.monthly.CalendarType
import io.github.tcompose_date_picker.TKDatePicker
import io.github.tcompose_date_picker.config.TextFieldType
import io.github.tcompose_date_picker.extensions.toEpochMillis
import io.github.tcompose_date_picker.extensions.toLocalDate

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

@Composable
fun CalendarTopBarStyle1 (
    modifier: Modifier = Modifier,
    useAdaptive: Boolean,
    selectedMonth: LocalDate,
    textStyle: TextStyle,
    onMonthChanged: (LocalDate) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    isDialogOpen: (Boolean) -> Unit,
    isTwoWeeksSupport: Boolean,
    isMonthlyView: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onToggleViewMode: () -> Unit,
    prevIcon: ImageVector? = null,
    nextIcon:ImageVector? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = prevIcon ?: Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous"
            )
        }

        TKDatePicker(
            useAdaptive = useAdaptive,
            textFieldType = TextFieldType.Custom { textModifier ->
                Text(
                    text = "${selectedMonth.month.name} ${selectedMonth.year}",
                    style = textStyle,
                    modifier = textModifier
                )
            },
            onDateSelected = { date ->
                date?.toEpochMillis()?.let { millis ->
                    val newDate = kotlin.time.Instant.fromEpochMilliseconds(millis)
                        .toLocalDate(TimeZone.currentSystemDefault())
                    onMonthChanged(newDate)
                    onDateSelected(newDate)
                }
            },
            onDismiss = {},
            isDialogOpen = isDialogOpen,
        )

        if (isTwoWeeksSupport) {
            Spacer(Modifier.weight(1f))
            CalendarType(isTwoWeeksView = !isMonthlyView) {
                onToggleViewMode()
            }
        }

        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = nextIcon ?: Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}