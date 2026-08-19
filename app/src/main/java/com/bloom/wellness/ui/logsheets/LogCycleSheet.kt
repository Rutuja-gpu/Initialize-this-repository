package com.bloom.wellness.ui.logsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bloom.wellness.ui.components.BloomBottomSheet
import com.bloom.wellness.ui.components.BloomPrimaryButton
import com.bloom.wellness.ui.home.HomeViewModel
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTint
import com.bloom.wellness.ui.theme.BloomTextSecondary
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogCycleSheet(viewModel: HomeViewModel, sheetState: SheetState, onDismiss: () -> Unit) {
    var visibleMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(viewModel.dailyLog.lastPeriodStart) }
    val nextExpected = selectedDate?.plusDays(28)

    BloomBottomSheet(title = "Log Cycle", onDismiss = onDismiss, sheetState = sheetState) {
        Text(
            "When did your last period start?",
            style = MaterialTheme.typography.bodyMedium,
            color = BloomTextSecondary,
            modifier = Modifier.padding(bottom = BloomDimens.SpaceLg)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { visibleMonth = visibleMonth.minusMonths(1) }) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Previous month")
            }
            Text(
                "${visibleMonth.month.getDisplayName(TextStyle.FULL, Locale.US)} ${visibleMonth.year}",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { visibleMonth = visibleMonth.plusMonths(1) }) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = "Next month")
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(top = BloomDimens.SpaceSm)) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEach { label ->
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = BloomTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        val firstDay = visibleMonth.atDay(1)
        val leadingBlanks = (firstDay.dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
        val daysInMonth = visibleMonth.lengthOfMonth()
        val totalCells = leadingBlanks + daysInMonth
        val rows = (totalCells + 6) / 7

        Column(modifier = Modifier.padding(top = BloomDimens.SpaceXs)) {
            repeat(rows) { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(7) { col ->
                        val cellIndex = row * 7 + col
                        val dayNumber = cellIndex - leadingBlanks + 1
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (dayNumber in 1..daysInMonth) {
                                val date = visibleMonth.atDay(dayNumber)
                                val isSelected = date == selectedDate
                                val isNextExpected = date == nextExpected
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .fillMaxWidth()
                                        .background(
                                            color = when {
                                                isSelected -> BloomMaroon
                                                isNextExpected -> BloomRoseTint
                                                else -> androidx.compose.ui.graphics.Color.Transparent
                                            },
                                            shape = CircleShape
                                        )
                                        .clickable { selectedDate = date },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        dayNumber.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(modifier = Modifier.padding(top = BloomDimens.SpaceSm)) {
            LegendDot(color = BloomMaroon, label = "Last period start")
            LegendDot(color = BloomRoseTint, label = "Next expected", modifier = Modifier.padding(start = BloomDimens.SpaceLg))
        }

        Text(
            "Tap a date on the calendar above and Bloom will estimate your next period and let you know if it looks delayed.",
            style = MaterialTheme.typography.bodySmall,
            color = BloomTextSecondary,
            modifier = Modifier.padding(top = BloomDimens.SpaceMd, bottom = BloomDimens.SpaceLg)
        )

        BloomPrimaryButton(
            text = "Done",
            onClick = {
                selectedDate?.let { viewModel.setLastPeriodStart(it) }
                onDismiss()
            }
        )
    }
}

@Composable
private fun LegendDot(color: androidx.compose.ui.graphics.Color, label: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
                .padding(end = BloomDimens.SpaceXs)
        )
        Text(label, style = MaterialTheme.typography.labelSmall, color = BloomTextSecondary, modifier = Modifier.padding(start = BloomDimens.SpaceXs))
    }
}
