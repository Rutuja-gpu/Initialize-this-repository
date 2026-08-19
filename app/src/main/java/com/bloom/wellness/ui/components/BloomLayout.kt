package com.bloom.wellness.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTint
import com.bloom.wellness.ui.theme.BloomSurface
import com.bloom.wellness.ui.theme.BloomTextSecondary

/** A rounded content card matching the Figma card style used for every section on Home/Insights/Settings. */
@Composable
fun BloomCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BloomSurface,
    padding: PaddingValues = PaddingValues(BloomDimens.SpaceLg),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = RoundedCornerShape(BloomDimens.CardCorner)
    ) {
        Column(modifier = Modifier.padding(padding), content = content)
    }
}

/** Section title (serif italic) + optional muted subtitle, used at the top of every screen. */
@Composable
fun ScreenHeader(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.headlineMedium, color = BloomMaroon)
        if (subtitle != null) {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = BloomTextSecondary,
                modifier = Modifier.padding(top = BloomDimens.SpaceXs)
            )
        }
    }
}

/** Circular tinted icon container used in front of nearly every list row. */
@Composable
fun IconChip(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = BloomMaroon,
    background: Color = BloomRoseTint,
    size: Dp = BloomDimens.IconChipSize
) {
    Box(
        modifier = modifier
            .size(size)
            .background(background, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(size * 0.5f))
    }
}

/** The 4-segment progress indicator + optional back chevron shown at the top of each onboarding step. */
@Composable
fun OnboardingProgressBar(
    step: Int,
    totalSteps: Int,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back", tint = BloomMaroon)
            }
        } else {
            Box(modifier = Modifier.size(48.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(BloomDimens.SpaceSm),
            modifier = Modifier
                .weight(1f)
                .padding(end = BloomDimens.SpaceLg)
        ) {
            repeat(totalSteps) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            color = if (index < step) BloomMaroon else BloomRoseTint,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

/** Small dot pagination indicator, e.g. the PCOS FAQ carousel on the dashboard. */
@Composable
fun DotsIndicator(count: Int, selectedIndex: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == selectedIndex) 7.dp else 5.dp)
                    .background(
                        if (index == selectedIndex) BloomMaroon else BloomRoseTint,
                        CircleShape
                    )
            )
        }
    }
}

/** A tappable row: optional icon chip, title (+ optional trailing text), chevron. Used across Insights/Learn/Settings lists. */
@Composable
fun ChevronRow(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    trailingText: String? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = BloomDimens.SpaceMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            IconChip(icon = icon, size = 36.dp)
            Spacer(Modifier.width(BloomDimens.SpaceSm))
        }
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (trailingText != null) {
            Text(
                trailingText,
                style = MaterialTheme.typography.bodySmall,
                color = BloomTextSecondary,
                modifier = Modifier.padding(end = BloomDimens.SpaceXs)
            )
        }
        Icon(
            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = BloomTextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}
