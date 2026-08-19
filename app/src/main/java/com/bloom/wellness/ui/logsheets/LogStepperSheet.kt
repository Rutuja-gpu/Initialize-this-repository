package com.bloom.wellness.ui.logsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bloom.wellness.data.model.DailyTip
import com.bloom.wellness.data.model.TrackingCategory
import com.bloom.wellness.ui.components.BloomBottomSheet
import com.bloom.wellness.ui.components.BloomCard
import com.bloom.wellness.ui.components.IconChip
import com.bloom.wellness.ui.components.StepperControl
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomTanCard
import com.bloom.wellness.ui.theme.BloomTextSecondary

/** Shared layout for the four simple quantity log sheets: Movement, Water, Relaxation, Sleep. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogStepperSheet(
    title: String,
    category: TrackingCategory,
    value: Int,
    target: Int,
    unit: String,
    tip: DailyTip,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    onQuickAdd: () -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    BloomBottomSheet(title = title, onDismiss = onDismiss, sheetState = sheetState) {
        Text(
            "Today's ${category.title.lowercase()}",
            style = MaterialTheme.typography.bodyMedium,
            color = BloomTextSecondary
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = BloomDimens.SpaceSm)) {
            StepperControl(value = value, unit = unit, onDecrement = onDecrement, onIncrement = onIncrement)
        }
        Text(
            "Goal $target $unit · adjust in Settings",
            style = MaterialTheme.typography.bodySmall,
            color = BloomTextSecondary,
            modifier = Modifier.padding(top = BloomDimens.SpaceXs, bottom = BloomDimens.SpaceLg)
        )

        BloomCard(backgroundColor = BloomTanCard) {
            Row {
                IconChip(icon = category.icon, background = androidx.compose.ui.graphics.Color.White)
                Column(modifier = Modifier.padding(start = BloomDimens.SpaceMd).weight(1f)) {
                    Text(tip.title, style = MaterialTheme.typography.titleMedium, color = BloomMaroon)
                    Text(
                        tip.body,
                        style = MaterialTheme.typography.bodySmall,
                        color = BloomTextSecondary,
                        modifier = Modifier.padding(top = BloomDimens.SpaceXs)
                    )
                    AssistChip(
                        onClick = onQuickAdd,
                        label = { Text("+${tip.quickAddAmount} ${tip.quickAddUnit}") },
                        colors = AssistChipDefaults.assistChipColors(labelColor = BloomMaroon),
                        modifier = Modifier.padding(top = BloomDimens.SpaceSm)
                    )
                }
            }
        }
    }
}
