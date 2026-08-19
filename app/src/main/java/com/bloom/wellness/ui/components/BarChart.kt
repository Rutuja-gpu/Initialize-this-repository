package com.bloom.wellness.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTint
import com.bloom.wellness.ui.theme.BloomTextSecondary

/** Weekly bar chart, e.g. the Insights "Your score" M–S breakdown. Values are 0f..1f fractions. */
@Composable
fun WeekBarChart(
    labels: List<String>,
    values: List<Float>,
    modifier: Modifier = Modifier,
    barHeight: androidx.compose.ui.unit.Dp = 80.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        labels.forEachIndexed { index, label ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(
                    modifier = Modifier
                        .width(BloomDimens.SpaceLg)
                        .height(barHeight)
                ) {
                    val fraction = values.getOrElse(index) { 0f }.coerceIn(0f, 1f)
                    drawRoundRect(
                        color = BloomRoseTint,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                    )
                    val filledHeight = size.height * fraction
                    drawRoundRect(
                        color = BloomMaroon,
                        topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - filledHeight),
                        size = androidx.compose.ui.geometry.Size(size.width, filledHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                    )
                }
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = BloomTextSecondary,
                    modifier = Modifier.padding(top = BloomDimens.SpaceXs)
                )
            }
        }
    }
}

/** Compact inline severity bar used in the Insights "Symptom patterns" list. */
@Composable
fun InlineSeverityBar(fraction: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.height(20.dp)) {
        val barCount = 7
        val gap = 3.dp.toPx()
        val barWidth = (size.width - gap * (barCount - 1)) / barCount
        val filledCount = (fraction * barCount).toInt().coerceIn(0, barCount)
        repeat(barCount) { index ->
            val x = index * (barWidth + gap)
            val filled = index < filledCount
            drawRoundRect(
                color = if (filled) BloomMaroon else BloomRoseTint,
                topLeft = androidx.compose.ui.geometry.Offset(x, 0f),
                size = androidx.compose.ui.geometry.Size(barWidth, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
            )
        }
    }
}
