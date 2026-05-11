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
import kotlin.time.Instant
enum class CalendarTopBarStyle {
    STYLE1, STYLE2
}
@Composable
fun CalendarTopBarStyleFormat (
    style: CalendarTopBarStyle,
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
    when(style){
        CalendarTopBarStyle.STYLE1 -> CalendarTopBarStyle1(
            modifier = modifier,
            useAdaptive = useAdaptive,
            selectedMonth = selectedMonth,
            textStyle = textStyle,
            onMonthChanged = onMonthChanged,
            onDateSelected = onDateSelected,
            isDialogOpen = isDialogOpen,
            isTwoWeeksSupport = isTwoWeeksSupport,
            isMonthlyView = isMonthlyView,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onToggleViewMode = onToggleViewMode,
            prevIcon = prevIcon,
            nextIcon = nextIcon
        )
        CalendarTopBarStyle.STYLE2 -> TODO()
    }
}