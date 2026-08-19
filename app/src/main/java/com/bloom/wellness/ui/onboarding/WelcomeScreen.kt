package com.bloom.wellness.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bloom.wellness.ui.components.BloomFlowerLogo
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.components.BloomSparkleField
import com.bloom.wellness.ui.components.OnboardingProgressBar
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomTextSecondary

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BloomCream)
                .padding(BloomDimens.ScreenPadding)
        ) {
            OnboardingProgressBar(step = 1, totalSteps = 4, onBack = null)

            BloomSparkleField(modifier = Modifier.fillMaxWidth())

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BloomFlowerLogo(size = 88.dp)
                Text(
                    "Welcome to Bloom",
                    style = MaterialTheme.typography.headlineLarge,
                    color = BloomMaroon,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = BloomDimens.SpaceXl)
                )
                Text(
                    "A simple daily companion for managing PCOS — track your cycle, meals, movement, water, relaxation and sleep.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BloomTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = BloomDimens.SpaceMd, start = BloomDimens.SpaceLg, end = BloomDimens.SpaceLg)
                )
            }

            BloomPrimaryButton(text = "Get started", onClick = onGetStarted)
        }
    }
}
