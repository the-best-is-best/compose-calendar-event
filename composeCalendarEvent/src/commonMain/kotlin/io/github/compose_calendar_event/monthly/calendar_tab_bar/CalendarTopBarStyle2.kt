package io.github.compose_calendar_event.monthly.calendar_tab_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.compose_calendar_event.monthly.CalendarType
import io.github.tcompose_date_picker.TKDatePicker
import io.github.tcompose_date_picker.config.TextFieldType
import io.github.tcompose_date_picker.extensions.toEpochMillis
import io.github.tcompose_date_picker.extensions.toLocalDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

@Composable
fun CalendarTopBarStyle2(
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
    nextIcon: ImageVector? = null,
    prevAndNextIconColor: Color,
    twoWeeksTextStyle: TextStyle,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        TKDatePicker(
            useAdaptive = useAdaptive,
            textFieldType = TextFieldType.Custom { textModifier ->
                Text(
                    text = selectedMonth.month.name.lowercase()
                        .replaceFirstChar { it.uppercase() } + " ${selectedMonth.year}",
                    style = textStyle,

                    modifier = textModifier
                )
            },
            onDateSelected = { date ->
                date?.toEpochMillis()?.let { millis ->
                    val newDate = Instant.fromEpochMilliseconds(millis)
                        .toLocalDate(TimeZone.currentSystemDefault())
                    onMonthChanged(newDate)
                    onDateSelected(newDate)
                }
            },
            onDismiss = {},
            isDialogOpen = isDialogOpen,
        )
        Row {
            if (isTwoWeeksSupport) {
                CalendarType(isTwoWeeksView = !isMonthlyView, textStyle = twoWeeksTextStyle) {
                    onToggleViewMode()
                }
            }

            IconButton(onClick = onPreviousClick) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    tint = prevAndNextIconColor,
                    imageVector = prevIcon ?: Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous"
                )
            }


            IconButton(onClick = onNextClick) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    tint = prevAndNextIconColor,
                    imageVector = nextIcon ?: Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }


    }
}