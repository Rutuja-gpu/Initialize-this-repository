package com.bloom.wellness.ui.logsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bloom.wellness.data.model.JourneyStage
import com.bloom.wellness.ui.components.BloomBottomSheet
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.components.BloomTextField
import com.bloom.wellness.ui.components.RadioOptionRow
import com.bloom.wellness.ui.home.HomeViewModel
import com.bloom.wellness.ui.theme.BloomDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileSheet(viewModel: HomeViewModel, sheetState: SheetState, onDismiss: () -> Unit) {
    val profile = viewModel.profile.value
    var name by remember(profile.firstName) { mutableStateOf(profile.firstName) }
    var stage by remember(profile.journeyStage) { mutableStateOf(profile.journeyStage) }

    BloomBottomSheet(title = "Edit Profile", onDismiss = onDismiss, sheetState = sheetState) {
        BloomTextField(value = name, onValueChange = { name = it }, placeholder = "Your first name")

        Column(modifier = Modifier.padding(top = BloomDimens.SpaceLg, bottom = BloomDimens.SpaceLg)) {
            androidx.compose.material3.Text(
                "Journey stage",
                style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                color = com.bloom.wellness.ui.theme.BloomTextSecondary
            )
            JourneyStage.entries.forEach { entry ->
                RadioOptionRow(
                    title = entry.title,
                    subtitle = entry.subtitle,
                    selected = stage == entry,
                    onClick = { stage = entry }
                )
            }
        }

        BloomPrimaryButton(
            text = "Save",
            onClick = {
                viewModel.setFirstName(name.trim())
                stage?.let { viewModel.setJourneyStage(it) }
                onDismiss()
            }
        )
    }
}
