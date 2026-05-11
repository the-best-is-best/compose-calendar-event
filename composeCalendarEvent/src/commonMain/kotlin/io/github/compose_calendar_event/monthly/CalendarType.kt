package io.github.compose_calendar_event.monthly

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle


@Composable
internal fun CalendarType(
    isTwoWeeksView: Boolean,
    textStyle: TextStyle,
    onSelectionChange: (Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Text(if (isTwoWeeksView) "2 Weeks" else "Month", style = textStyle)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },

            ) {
            DropdownMenuItem(

                text = { Text("Month", style = textStyle) },
                onClick = {
                    onSelectionChange(false)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("2 Weeks", style = textStyle) },
                onClick = {
                    onSelectionChange(true)
                    expanded = false
                }
            )
        }
    }
}