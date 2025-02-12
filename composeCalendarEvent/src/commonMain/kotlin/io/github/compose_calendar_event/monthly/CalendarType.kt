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


@Composable
fun CalendarType(isTwoWeeksView: Boolean, onSelectionChange: (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Text(if (isTwoWeeksView) "2 Weeks" else "Month")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Month") },
                onClick = {
                    onSelectionChange(false)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("2 Weeks") },
                onClick = {
                    onSelectionChange(true)
                    expanded = false
                }
            )
        }
    }
}