package com.bloom.wellness.ui.logsheets

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bloom.wellness.data.model.ReminderPeriod
import com.bloom.wellness.ui.components.BloomBottomSheet
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.components.RadioOptionRow
import com.bloom.wellness.ui.home.HomeViewModel
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTint
import com.bloom.wellness.ui.theme.BloomTextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimeSheet(viewModel: HomeViewModel, sheetState: SheetState, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val profile = viewModel.profile.value
    var period by remember(profile.reminderPeriod) { mutableStateOf(profile.reminderPeriod) }
    var hour by remember(profile.reminderCustomHour) { mutableStateOf(profile.reminderCustomHour) }
    var minute by remember(profile.reminderCustomMinute) { mutableStateOf(profile.reminderCustomMinute) }

    BloomBottomSheet(title = "Reminder Time", onDismiss = onDismiss, sheetState = sheetState) {
        Text(
            "When should Bloom nudge you to log your day?",
            style = MaterialTheme.typography.bodyMedium,
            color = BloomTextSecondary,
            modifier = Modifier.padding(bottom = BloomDimens.SpaceLg)
        )
        ReminderPeriod.entries.filter { it != ReminderPeriod.CUSTOM }.forEach { entry ->
            RadioOptionRow(
                title = entry.title,
                subtitle = entry.subtitle,
                selected = period == entry,
                onClick = { period = entry }
            )
        }

        Text(
            "Or set a custom time",
            style = MaterialTheme.typography.titleSmall,
            color = BloomTextSecondary,
            modifier = Modifier.padding(top = BloomDimens.SpaceLg, bottom = BloomDimens.SpaceSm)
        )
        Card(
            onClick = {
                TimePickerDialog(context, { _, h, m -> hour = h; minute = m; period = ReminderPeriod.CUSTOM }, hour, minute, false).show()
            },
            shape = RoundedCornerShape(BloomDimens.CardCornerSmall),
            colors = CardDefaults.cardColors(
                containerColor = if (period == ReminderPeriod.CUSTOM) BloomRoseTint else Color.Transparent
            ),
            border = BorderStroke(1.dp, BloomRoseTint),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(BloomDimens.SpaceLg), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = BloomMaroon)
                Text(formatClock(hour, minute), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = BloomDimens.SpaceSm))
            }
        }

        BloomPrimaryButton(
            text = "Save",
            onClick = {
                if (period == ReminderPeriod.CUSTOM) {
                    viewModel.setReminderCustomTime(hour, minute)
                } else {
                    viewModel.setReminderPeriod(period)
                }
                onDismiss()
            },
            modifier = Modifier.padding(top = BloomDimens.SpaceLg)
        )
    }
}

private fun formatClock(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return String.format(Locale.US, "%d:%02d %s", displayHour, minute, amPm)
}
