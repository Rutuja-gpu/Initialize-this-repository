package com.bloom.wellness.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bloom.wellness.data.model.TrackingCategory
import com.bloom.wellness.ui.components.BloomCard
import com.bloom.wellness.ui.components.ChevronRow
import com.bloom.wellness.ui.components.IconChip
import com.bloom.wellness.ui.components.ScreenHeader
import com.bloom.wellness.ui.components.StepperControl
import com.bloom.wellness.ui.components.ToggleRow
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomTextSecondary

@Composable
fun SettingsScreen(viewModel: HomeViewModel) {
    val profile by viewModel.profile.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BloomCream),
        contentPadding = PaddingValues(BloomDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(BloomDimens.SpaceLg)
    ) {
        item { ScreenHeader("Settings", "Your profile and daily goal targets") }

        item {
            BloomCard {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    IconChip(icon = Icons.Outlined.Person, size = 48.dp)
                    Column(modifier = Modifier.weight(1f).padding(start = BloomDimens.SpaceMd)) {
                        Text(
                            profile.firstName.ifBlank { "Your name" },
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "PCOS journey · ${profile.journeyStage?.title ?: "Not set"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = BloomTextSecondary
                        )
                    }
                }
                ChevronRow(title = "Edit profile", onClick = { viewModel.openSheet(ActiveSheet.EDIT_PROFILE) })
            }
        }

        item {
            Text("What you're tracking", style = MaterialTheme.typography.titleLarge, color = BloomMaroon)
        }
        item {
            BloomCard {
                TrackingCategory.entries.forEach { category ->
                    ToggleRow(
                        icon = category.icon,
                        title = category.title,
                        description = null,
                        checked = profile.trackingEnabled[category] ?: true,
                        onCheckedChange = { viewModel.setTrackingEnabled(category, it) }
                    )
                }
            }
        }

        item {
            Text("Daily goal targets", style = MaterialTheme.typography.titleLarge, color = BloomMaroon)
        }
        item {
            BloomCard {
                TrackingCategory.goalCategories.forEach { category ->
                    val target = profile.goalTargets[category] ?: category.defaultTarget
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = BloomDimens.SpaceSm)
                    ) {
                        Text(category.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                        StepperControl(
                            value = target,
                            unit = category.unitLabel,
                            onDecrement = { viewModel.setGoalTarget(category, target - 1) },
                            onIncrement = { viewModel.setGoalTarget(category, target + 1) }
                        )
                    }
                }
            }
        }

        item {
            BloomCard {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    IconChip(icon = Icons.Outlined.Notifications, size = 36.dp)
                    Column(modifier = Modifier.weight(1f).padding(start = BloomDimens.SpaceMd)) {
                        Text("Notifications", style = MaterialTheme.typography.titleMedium)
                        Text("Daily reminders to log", style = MaterialTheme.typography.bodySmall, color = BloomTextSecondary)
                    }
                    Switch(
                        checked = profile.remindersEnabled,
                        onCheckedChange = { viewModel.setRemindersEnabled(it) },
                        colors = SwitchDefaults.colors(checkedTrackColor = BloomMaroon)
                    )
                }
                ChevronRow(
                    title = "Reminder time",
                    trailingText = reminderTimeLabel(profile),
                    onClick = { viewModel.openSheet(ActiveSheet.REMINDER_TIME) }
                )
                ChevronRow(
                    title = "Live notification preview",
                    icon = Icons.Outlined.PlayCircleOutline,
                    trailingText = "See live lock-screen updates",
                    onClick = { viewModel.openSheet(ActiveSheet.NOTIFICATION_PREVIEW) }
                )
            }
        }

        item {
            Text(
                "App version  ·  1.0 · Prototype",
                style = MaterialTheme.typography.bodySmall,
                color = BloomTextSecondary
            )
        }

        item {
            OutlinedButton(
                onClick = {},
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BloomMaroon),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign out")
            }
        }

        item { Spacer(Modifier.height(BloomDimens.SpaceLg)) }
    }
}

private fun reminderTimeLabel(profile: com.bloom.wellness.data.BloomProfile): String {
    val period = profile.reminderPeriod
    return if (period == com.bloom.wellness.data.model.ReminderPeriod.CUSTOM) {
        formatClock(profile.reminderCustomHour, profile.reminderCustomMinute)
    } else {
        formatClock(period.hour, period.minute)
    }
}

private fun formatClock(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return String.format(java.util.Locale.US, "%d:%02d %s", displayHour, minute, amPm)
}

