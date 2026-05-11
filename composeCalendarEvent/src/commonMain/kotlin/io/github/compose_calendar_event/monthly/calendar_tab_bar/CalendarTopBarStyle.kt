package io.github.compose_calendar_event.monthly.calendar_tab_bar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import io.github.compose_calendar_event.monthly.enums.CalendarTopBarStyleEnum
import kotlinx.datetime.LocalDate


@Composable
fun CalendarTopBarStyleFormat(
    style: CalendarTopBarStyleEnum,
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
    when (style) {
        CalendarTopBarStyleEnum.STYLE1 -> CalendarTopBarStyle1(
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
            nextIcon = nextIcon,
            prevAndNextIconColor = prevAndNextIconColor,
            twoWeeksTextStyle = twoWeeksTextStyle,

            )

        CalendarTopBarStyleEnum.STYLE2 -> CalendarTopBarStyle2(
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
            nextIcon = nextIcon,
            prevAndNextIconColor = prevAndNextIconColor,
            twoWeeksTextStyle = twoWeeksTextStyle,

            )
    }
}