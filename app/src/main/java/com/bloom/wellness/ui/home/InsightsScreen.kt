package com.bloom.wellness.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bloom.wellness.data.model.TrackingCategory
import com.bloom.wellness.data.model.symptomScales
import com.bloom.wellness.ui.components.BloomCard
import com.bloom.wellness.ui.components.ChevronRow
import com.bloom.wellness.ui.components.InlineSeverityBar
import com.bloom.wellness.ui.components.ScreenHeader
import com.bloom.wellness.ui.components.WeekBarChart
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomTextSecondary

@Composable
fun InsightsScreen(viewModel: HomeViewModel) {
    val profile by viewModel.profile.collectAsState()
    val dailyLog = viewModel.dailyLog
    val weekScore = viewModel.goalsCompletedCount().let { completed ->
        val total = TrackingCategory.goalCategories.count { profile.trackingEnabled[it] != false }
        if (total == 0) 0 else (completed * 100) / total
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BloomCream),
        contentPadding = PaddingValues(BloomDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(BloomDimens.SpaceLg)
    ) {
        item { ScreenHeader("Insights", "Your week at a glance, and what to try next") }

        item {
            BloomCard {
                Text("Your score", style = MaterialTheme.typography.titleLarge, color = BloomMaroon)
                Text(
                    "$weekScore%",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = BloomMaroon,
                    modifier = Modifier.padding(top = BloomDimens.SpaceXs)
                )
                Text("Week average", style = MaterialTheme.typography.bodySmall, color = BloomTextSecondary)
                Spacer(Modifier.height(BloomDimens.SpaceLg))
                WeekBarChart(
                    labels = listOf("M", "T", "W", "T", "F", "S", "S"),
                    values = listOf(0.3f, 0.5f, 0.4f, 0.7f, 0.2f, 0f, weekScore / 100f)
                )
            }
        }

        if (profile.trackingEnabled[TrackingCategory.CYCLE] != false) {
            item {
                BloomCard {
                    ChevronRow(
                        title = "Cycle",
                        icon = TrackingCategory.CYCLE.icon,
                        trailingText = dailyLog.lastPeriodStart?.toString() ?: "No cycle logged yet",
                        onClick = { viewModel.openSheet(ActiveSheet.CYCLE) }
                    )
                }
            }
        }

        item {
            BloomCard {
                Text("Daily summary", style = MaterialTheme.typography.titleLarge, color = BloomMaroon)
                Text("Today", style = MaterialTheme.typography.titleSmall, color = BloomTextSecondary, modifier = Modifier.padding(top = BloomDimens.SpaceSm))
                val progressPct = if (TrackingCategory.goalCategories.isEmpty()) 0 else
                    (viewModel.goalsCompletedCount() * 100) / TrackingCategory.goalCategories.size
                Text(
                    "$progressPct%",
                    style = MaterialTheme.typography.headlineSmall,
                    color = BloomMaroon
                )
                Text(
                    if (progressPct == 0) "Nothing logged yet today — no rush, tap a goal below when you're ready."
                    else "Nice work — keep going to hit today's goals.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BloomTextSecondary
                )
            }
        }

        if (profile.trackingEnabled[TrackingCategory.SYMPTOM_CHECKIN] != false) {
            item {
                BloomCard {
                    Text("Symptom patterns", style = MaterialTheme.typography.titleLarge, color = BloomMaroon)
                    Spacer(Modifier.height(BloomDimens.SpaceSm))
                    symptomScales.forEach { scale ->
                        val logged = dailyLog.symptoms[scale.key]
                        val severity = logged?.let { scale.options.indexOf(it) / (scale.options.size - 1).toFloat() } ?: 0f
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = BloomDimens.SpaceSm),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(scale.title, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    logged?.let { "Currently: $it" } ?: "Not logged yet",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BloomTextSecondary
                                )
                            }
                            InlineSeverityBar(
                                fraction = severity,
                                modifier = Modifier
                                    .width(90.dp)
                                    .padding(start = BloomDimens.SpaceMd)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(BloomDimens.SpaceLg)) }
    }
}
