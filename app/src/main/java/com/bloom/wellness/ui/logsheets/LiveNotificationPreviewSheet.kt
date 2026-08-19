package com.bloom.wellness.ui.logsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bloom.wellness.ui.components.BloomBottomSheet
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTint
import com.bloom.wellness.ui.theme.BloomTextSecondary
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val previewTabs = listOf("Meditation", "Daily goal", "Water")
private const val MEDITATION_SECONDS = 5 * 60

/**
 * A UI-only mock of a live-updating lock-screen notification (the kind used for order
 * tracking / workout apps). No real notification is posted — see README limitations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveNotificationPreviewSheet(sheetState: SheetState, onDismiss: () -> Unit) {
    var selectedTab by remember { mutableStateOf(previewTabs.first()) }
    var remainingSeconds by remember { mutableIntStateOf(MEDITATION_SECONDS) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running, remainingSeconds) {
        if (running && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        } else if (remainingSeconds == 0) {
            running = false
        }
    }

    BloomBottomSheet(title = "Live Notification Preview", onDismiss = onDismiss, sheetState = sheetState) {
        Text(
            "A preview of how Bloom could show live real-time updates on the lock screen.",
            style = MaterialTheme.typography.bodyMedium,
            color = BloomTextSecondary,
            modifier = Modifier.padding(bottom = BloomDimens.SpaceLg)
        )

        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(BloomDimens.SpaceSm)) {
            previewTabs.forEach { tab ->
                AssistChip(
                    onClick = { selectedTab = tab },
                    label = { Text(tab) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (tab == selectedTab) BloomRoseTint else Color.Transparent,
                        labelColor = BloomMaroon
                    )
                )
            }
        }

        LockScreenMock(
            remainingSeconds = remainingSeconds,
            modifier = Modifier.padding(vertical = BloomDimens.SpaceLg)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(BloomDimens.SpaceSm)) {
            BloomPrimaryButton(
                text = "Begin",
                onClick = { running = true },
                modifier = Modifier.weight(1f)
            )
            OutlinedButton(
                onClick = { running = false; remainingSeconds = MEDITATION_SECONDS },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BloomMaroon),
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
        }

        Text(
            "Updates live while a meditation session is running — same pattern as an order-tracking or workout notification.",
            style = MaterialTheme.typography.bodySmall,
            color = BloomTextSecondary,
            modifier = Modifier.padding(top = BloomDimens.SpaceMd)
        )
    }
}

@Composable
private fun LockScreenMock(remainingSeconds: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1E), RoundedCornerShape(BloomDimens.CardCorner))
            .padding(BloomDimens.SpaceLg)
    ) {
        Text(
            LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a", Locale.US)),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BloomDimens.SpaceMd)
                .background(Color(0xFF2C2C2E), RoundedCornerShape(BloomDimens.CardCornerSmall))
                .padding(BloomDimens.SpaceMd),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Bloom now", style = MaterialTheme.typography.labelSmall, color = Color(0xFFB8B0AE))
                Text(
                    "Meditation: ${remainingSeconds / 60}:${(remainingSeconds % 60).toString().padStart(2, '0')} remaining",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}
