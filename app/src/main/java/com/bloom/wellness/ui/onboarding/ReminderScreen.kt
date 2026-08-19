package com.bloom.wellness.ui.onboarding

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bloom.wellness.data.model.ReminderPeriod
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.components.BloomTextLink
import com.bloom.wellness.ui.components.IconChip
import com.bloom.wellness.ui.components.OnboardingProgressBar
import com.bloom.wellness.ui.components.RadioOptionRow
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTint
import com.bloom.wellness.ui.theme.BloomTextSecondary
import java.util.Locale

@Composable
fun ReminderScreen(
    selectedPeriod: ReminderPeriod?,
    customHour: Int,
    customMinute: Int,
    onPeriodSelected: (ReminderPeriod) -> Unit,
    onCustomTimeSelected: (hour: Int, minute: Int) -> Unit,
    onBack: () -> Unit,
    onEnterBloom: () -> Unit,
    onSkip: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BloomCream)
            .padding(BloomDimens.ScreenPadding)
    ) {
        OnboardingProgressBar(step = 4, totalSteps = 4, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconChip(icon = Icons.Outlined.AccessTime, modifier = Modifier.padding(top = BloomDimens.SpaceLg))
            Text(
                "When should we remind you?",
                style = MaterialTheme.typography.headlineSmall,
                color = BloomMaroon,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = BloomDimens.SpaceMd)
            )
            Text(
                "We'll send one daily nudge to how you're doing.",
                style = MaterialTheme.typography.bodyMedium,
                color = BloomTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = BloomDimens.SpaceSm)
            )

            Column(modifier = Modifier.fillMaxWidth().padding(top = BloomDimens.SpaceXl)) {
                Text(
                    "Pick a time of day",
                    style = MaterialTheme.typography.titleSmall,
                    color = BloomTextSecondary
                )
                ReminderPeriod.entries.filter { it != ReminderPeriod.CUSTOM }.forEach { period ->
                    RadioOptionRow(
                        title = period.title,
                        subtitle = period.subtitle,
                        selected = selectedPeriod == period,
                        onClick = { onPeriodSelected(period) }
                    )
                }

                Text(
                    "Or set a custom time",
                    style = MaterialTheme.typography.titleSmall,
                    color = BloomTextSecondary,
                    modifier = Modifier.padding(top = BloomDimens.SpaceLg, bottom = BloomDimens.SpaceSm)
                )
                CustomTimeField(
                    hour = customHour,
                    minute = customMinute,
                    isSelected = selectedPeriod == ReminderPeriod.CUSTOM,
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour, minute -> onCustomTimeSelected(hour, minute) },
                            customHour,
                            customMinute,
                            false
                        ).show()
                    }
                )
            }
        }

        BloomPrimaryButton(text = "Enter Bloom", onClick = onEnterBloom, modifier = Modifier.padding(top = BloomDimens.SpaceLg))
        BloomTextLink(
            text = "I'll set this up later",
            onClick = onSkip,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun CustomTimeField(hour: Int, minute: Int, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(BloomDimens.CardCornerSmall),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BloomRoseTint else androidx.compose.ui.graphics.Color.Transparent
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BloomRoseTint),
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTimeFieldContent(hour, minute)
    }
}

@Composable
private fun CustomTimeFieldContent(hour: Int, minute: Int) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(BloomDimens.SpaceLg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = BloomMaroon)
        Text(
            formatTime(hour, minute),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = BloomDimens.SpaceSm)
        )
    }
}

private fun formatTime(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return String.format(Locale.US, "%d:%02d %s", displayHour, minute, amPm)
}
