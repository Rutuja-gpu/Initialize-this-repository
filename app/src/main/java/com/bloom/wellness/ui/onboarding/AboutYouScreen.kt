package com.bloom.wellness.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.components.BloomTextField
import com.bloom.wellness.ui.components.OnboardingProgressBar
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon

@Composable
fun AboutYouScreen(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BloomCream)
            .padding(BloomDimens.ScreenPadding)
    ) {
        OnboardingProgressBar(step = 2, totalSteps = 4, onBack = onBack)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "A little about you",
                style = MaterialTheme.typography.headlineMedium,
                color = BloomMaroon,
                textAlign = TextAlign.Center
            )
            Column(modifier = Modifier.padding(top = BloomDimens.SpaceXl)) {
                BloomTextField(
                    value = firstName,
                    onValueChange = onFirstNameChange,
                    placeholder = "Your first name"
                )
            }
        }

        BloomPrimaryButton(
            text = "Continue",
            onClick = onContinue,
            enabled = firstName.isNotBlank()
        )
    }
}
