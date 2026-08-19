package com.bloom.wellness.ui.logsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bloom.wellness.data.model.MealRating
import com.bloom.wellness.data.model.MealSlot
import com.bloom.wellness.ui.components.BloomBottomSheet
import com.bloom.wellness.ui.components.PillSelector
import com.bloom.wellness.ui.home.HomeViewModel
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogMealsSheet(viewModel: HomeViewModel, sheetState: SheetState, onDismiss: () -> Unit) {
    val dailyLog = viewModel.dailyLog
    val loggedCount = dailyLog.meals.values.count { it != null }

    BloomBottomSheet(title = "Log Meals", onDismiss = onDismiss, sheetState = sheetState) {
        Text(
            "How would you describe each meal today?",
            style = MaterialTheme.typography.bodyMedium,
            color = BloomTextSecondary,
            modifier = Modifier.padding(bottom = BloomDimens.SpaceLg)
        )
        MealSlot.entries.forEach { slot ->
            Column(modifier = Modifier.padding(bottom = BloomDimens.SpaceLg)) {
                Text(slot.label, style = MaterialTheme.typography.titleMedium)
                Column(modifier = Modifier.padding(top = BloomDimens.SpaceSm)) {
                    val ratingsTop = listOf(MealRating.LIGHT, MealRating.BALANCED)
                    val ratingsBottom = listOf(MealRating.INDULGENT, MealRating.SKIPPED)
                    val selected = dailyLog.meals[slot]
                    PillSelector(
                        options = ratingsTop.map { it.label },
                        selected = selected?.label,
                        onSelect = { label -> viewModel.logMeal(slot, MealRating.entries.first { it.label == label }) }
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(top = BloomDimens.SpaceSm))
                    PillSelector(
                        options = ratingsBottom.map { it.label },
                        selected = selected?.label,
                        onSelect = { label -> viewModel.logMeal(slot, MealRating.entries.first { it.label == label }) }
                    )
                }
            }
        }
        Text(
            "$loggedCount of ${MealSlot.entries.size} logged today",
            style = MaterialTheme.typography.bodySmall,
            color = BloomTextSecondary
        )
    }
}
