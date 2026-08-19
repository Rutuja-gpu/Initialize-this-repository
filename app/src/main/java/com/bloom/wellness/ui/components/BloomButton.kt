package com.bloom.wellness.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTintStrong
import com.bloom.wellness.ui.theme.BloomTextSecondary

/** The full-width filled maroon CTA used across onboarding and log sheets ("Get started", "Continue", "Save"...). */
@Composable
fun BloomPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(BloomDimens.ButtonHeight),
        shape = RoundedCornerShape(BloomDimens.ButtonCorner),
        colors = ButtonDefaults.buttonColors(
            containerColor = BloomMaroon,
            contentColor = BloomCream,
            disabledContainerColor = BloomRoseTintStrong,
            disabledContentColor = BloomCream
        )
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

/** A quiet text-only affordance, e.g. "I'll set this up later". */
@Composable
fun BloomTextLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(text, color = BloomTextSecondary, fontWeight = FontWeight.Medium)
    }
}
