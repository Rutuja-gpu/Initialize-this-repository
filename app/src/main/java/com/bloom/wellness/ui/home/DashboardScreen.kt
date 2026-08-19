package com.bloom.wellness.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bloom.wellness.data.model.TrackingCategory
import com.bloom.wellness.data.model.dailyTips
import com.bloom.wellness.data.model.learnSections
import com.bloom.wellness.ui.components.BloomCard
import com.bloom.wellness.ui.components.BloomFlowerLogo
import com.bloom.wellness.ui.components.BloomTextLink
import com.bloom.wellness.ui.components.ChevronRow
import com.bloom.wellness.ui.components.DotsIndicator
import com.bloom.wellness.ui.components.IconChip
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomTanCard
import com.bloom.wellness.ui.theme.BloomTextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DashboardScreen(viewModel: HomeViewModel) {
    val profile by viewModel.profile.collectAsState()
    val dailyLog = viewModel.dailyLog
    val trackedGoals = TrackingCategory.goalCategories.filter { profile.trackingEnabled[it] != false }
    val completedCount = viewModel.goalsCompletedCount()

    val displayName = profile.firstName.ifBlank { "there" }
    val today = remember { LocalDate.now() }

    var dismissedTip by remember { mutableStateOf<TrackingCategory?>(null) }
    val tipCategory = trackedGoals
        .filter { it in dailyTips && it != dismissedTip }
        .sortedBy { viewModel.goalProgress(it).let { (c, t) -> if (t == 0) 1f else c / t.toFloat() } }
        .firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BloomCream),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(BloomDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(BloomDimens.SpaceLg)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "GOOD MORNING,",
                        style = MaterialTheme.typography.labelMedium,
                        color = BloomTextSecondary
                    )
                    Text(
                        displayName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = BloomMaroon
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(40.dp)
                        .background(BloomTanCard, CircleShape)
                ) {
                    Icon(Icons.Outlined.NotificationsNone, contentDescription = "Notifications", tint = BloomMaroon)
                }
            }
        }

        item {
            TodaysGoalsCard(
                dateText = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.US)),
                completed = completedCount,
                total = trackedGoals.size
            )
        }

        if (profile.trackingEnabled[TrackingCategory.SYMPTOM_CHECKIN] != false) {
            item { PcosFaqCarousel() }
        }

        item {
            BloomCard {
                if (profile.trackingEnabled[TrackingCategory.CYCLE] != false) {
                    ChevronRow(
                        title = "Cycle",
                        icon = TrackingCategory.CYCLE.icon,
                        trailingText = dailyLog.lastPeriodStart?.let { "Logged" } ?: "Log your last period date",
                        onClick = { viewModel.openSheet(ActiveSheet.CYCLE) }
                    )
                }
                if (profile.trackingEnabled[TrackingCategory.SYMPTOM_CHECKIN] != false) {
                    ChevronRow(
                        title = "Symptom check-in",
                        icon = TrackingCategory.SYMPTOM_CHECKIN.icon,
                        trailingText = "Bloating, skin, mood, sleep",
                        onClick = { viewModel.openSheet(ActiveSheet.SYMPTOMS) }
                    )
                }
            }
        }

        if (trackedGoals.isNotEmpty()) {
            item {
                Text("Goal Progress", style = MaterialTheme.typography.titleLarge, color = BloomMaroon)
            }
            item {
                BloomCard {
                    trackedGoals.forEach { category ->
                        val (current, target) = viewModel.goalProgress(category)
                        ChevronRow(
                            title = category.title,
                            icon = category.icon,
                            trailingText = "$current/$target ${category.unitLabel}",
                            onClick = { viewModel.openSheet(category.toSheet()) }
                        )
                    }
                }
            }
        }

        if (tipCategory != null) {
            item {
                TodaysTipCard(
                    category = tipCategory,
                    onDismiss = { dismissedTip = tipCategory },
                    onMarkDone = {
                        val target = viewModel.goalProgress(tipCategory).second
                        val current = viewModel.goalProgress(tipCategory).first
                        val delta = (target - current).coerceAtLeast(0)
                        when (tipCategory) {
                            TrackingCategory.MOVEMENT -> viewModel.adjustMovement(delta)
                            TrackingCategory.WATER -> viewModel.adjustWater(delta)
                            TrackingCategory.RELAXATION -> viewModel.adjustRelaxation(delta)
                            TrackingCategory.SLEEP -> viewModel.adjustSleep(delta)
                            else -> Unit
                        }
                    }
                )
            }
        }

        item { Spacer(Modifier.height(BloomDimens.SpaceLg)) }
    }
}

private fun TrackingCategory.toSheet(): ActiveSheet = when (this) {
    TrackingCategory.MEALS -> ActiveSheet.MEALS
    TrackingCategory.MOVEMENT -> ActiveSheet.MOVEMENT
    TrackingCategory.WATER -> ActiveSheet.WATER
    TrackingCategory.RELAXATION -> ActiveSheet.RELAXATION
    TrackingCategory.SLEEP -> ActiveSheet.SLEEP
    TrackingCategory.CYCLE -> ActiveSheet.CYCLE
    TrackingCategory.SYMPTOM_CHECKIN -> ActiveSheet.SYMPTOMS
}

@Composable
private fun TodaysGoalsCard(dateText: String, completed: Int, total: Int) {
    BloomCard {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Today's Goals", style = MaterialTheme.typography.headlineSmall, color = BloomMaroon)
                Text(dateText, style = MaterialTheme.typography.bodySmall, color = BloomTextSecondary)
                Text(
                    "Let's get blooming",
                    style = MaterialTheme.typography.titleSmall,
                    color = BloomMaroon,
                    modifier = Modifier.padding(top = BloomDimens.SpaceXs)
                )
                Text(
                    "$completed of $total complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = BloomTextSecondary
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BloomFlowerLogo(size = 44.dp)
                Text("Start here", style = MaterialTheme.typography.labelSmall, color = BloomMaroon)
            }
        }
    }
}

@Composable
private fun PcosFaqCarousel() {
    val topics = remember { learnSections.first().topics.take(5) }
    val pagerState = rememberPagerState(pageCount = { topics.size })

    BloomCard {
        Text("PCOS FAQ", style = MaterialTheme.typography.titleLarge, color = BloomMaroon)
        Spacer(Modifier.height(BloomDimens.SpaceSm))
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
            Text(
                topics[page].question,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = BloomDimens.SpaceSm)
            )
        }
        DotsIndicator(
            count = topics.size,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BloomDimens.SpaceSm)
        )
    }
}

@Composable
private fun TodaysTipCard(category: TrackingCategory, onDismiss: () -> Unit, onMarkDone: () -> Unit) {
    val tip = dailyTips.getValue(category)
    BloomCard(backgroundColor = BloomTanCard) {
        Row {
            IconChip(icon = category.icon, background = BloomCream)
            Column(modifier = Modifier.padding(start = BloomDimens.SpaceMd).weight(1f)) {
                Text(tip.label, style = MaterialTheme.typography.labelMedium, color = BloomMaroon)
                Text(
                    tip.body,
                    style = MaterialTheme.typography.bodySmall,
                    color = BloomTextSecondary,
                    modifier = Modifier.padding(top = BloomDimens.SpaceXs)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = BloomDimens.SpaceMd),
                    horizontalArrangement = Arrangement.spacedBy(BloomDimens.SpaceSm)
                ) {
                    BloomTextLink(text = "Remind me later", onClick = onDismiss)
                    Box(modifier = Modifier.weight(1f))
                    androidx.compose.material3.TextButton(onClick = onMarkDone) {
                        Text("Mark as done", color = BloomMaroon, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
