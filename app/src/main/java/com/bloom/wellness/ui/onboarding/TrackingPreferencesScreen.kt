package com.bloom.wellness.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.bloom.wellness.data.model.TrackingCategory
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.components.IconChip
import com.bloom.wellness.ui.components.OnboardingProgressBar
import com.bloom.wellness.ui.components.ToggleRow
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomGold
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomTextSecondary

@Composable
fun TrackingPreferencesScreen(
    trackingEnabled: Map<TrackingCategory, Boolean>,
    onToggle: (TrackingCategory, Boolean) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BloomCream)
            .padding(BloomDimens.ScreenPadding)
    ) {
        OnboardingProgressBar(step = 3, totalSteps = 4, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconChip(
                icon = Icons.Outlined.AutoAwesome,
                tint = BloomGold,
                modifier = Modifier.padding(top = BloomDimens.SpaceLg)
            )
            Text(
                "What would you like to track daily?",
                style = MaterialTheme.typography.headlineSmall,
                color = BloomMaroon,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = BloomDimens.SpaceMd)
            )
            Text(
                "Pick the goals that matter to you — only these will show up on your dashboard. " +
                    "Partial progress still counts, and your flower fills in as you go.",
                style = MaterialTheme.typography.bodyMedium,
                color = BloomTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = BloomDimens.SpaceSm)
            )

            Column(modifier = Modifier.padding(top = BloomDimens.SpaceLg)) {
                TrackingCategory.entries.forEach { category ->
                    ToggleRow(
                        icon = category.icon,
                        title = "${category.title} · ${trackingSubLabel(category)}",
                        description = category.onboardingSubtitle,
                        checked = trackingEnabled[category] ?: true,
                        onCheckedChange = { onToggle(category, it) }
                    )
                }
            }

            Text(
                "Targets and what you track can be adjusted any time in Settings.",
                style = MaterialTheme.typography.bodySmall,
                color = BloomTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = BloomDimens.SpaceMd, bottom = BloomDimens.SpaceMd)
            )
        }

        BloomPrimaryButton(text = "Continue", onClick = onContinue)
    }
}

private fun trackingSubLabel(category: TrackingCategory): String = when (category) {
    TrackingCategory.MEALS -> "3 balanced meals"
    TrackingCategory.MOVEMENT -> "30 minutes"
    TrackingCategory.WATER -> "8 glasses"
    TrackingCategory.RELAXATION -> "15 minutes"
    TrackingCategory.SLEEP -> "8 hours"
    TrackingCategory.CYCLE -> "period dates"
    TrackingCategory.SYMPTOM_CHECKIN -> "bloating, skin, mood, sleep quality"
}
