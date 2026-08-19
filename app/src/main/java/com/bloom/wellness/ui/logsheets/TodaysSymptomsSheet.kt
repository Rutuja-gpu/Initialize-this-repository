package com.bloom.wellness.ui.logsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bloom.wellness.data.model.symptomScales
import com.bloom.wellness.ui.components.BloomBottomSheet
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.components.PillSelector
import com.bloom.wellness.ui.home.HomeViewModel
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodaysSymptomsSheet(viewModel: HomeViewModel, sheetState: SheetState, onDismiss: () -> Unit) {
    val dailyLog = viewModel.dailyLog

    BloomBottomSheet(title = "Today's symptoms", onDismiss = onDismiss, sheetState = sheetState) {
        Text(
            "These are separate from your daily goals — logging them just helps you (and eventually your doctor) spot patterns over time.",
            style = MaterialTheme.typography.bodyMedium,
            color = BloomTextSecondary,
            modifier = Modifier.padding(bottom = BloomDimens.SpaceLg)
        )
        symptomScales.forEach { scale ->
            Column(modifier = Modifier.padding(bottom = BloomDimens.SpaceLg)) {
                Text(scale.title, style = MaterialTheme.typography.titleMedium)
                PillSelector(
                    options = scale.options,
                    selected = dailyLog.symptoms[scale.key],
                    onSelect = { viewModel.setSymptom(scale.key, it) },
                    modifier = Modifier.padding(top = BloomDimens.SpaceSm)
                )
            }
        }
        BloomPrimaryButton(text = "Done", onClick = onDismiss)
    }
}
