package com.bloom.wellness.ui.logsheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.bloom.wellness.data.model.TrackingCategory
import com.bloom.wellness.data.model.dailyTips
import com.bloom.wellness.ui.home.ActiveSheet
import com.bloom.wellness.ui.home.HomeViewModel

/** Renders whichever "Log X" / edit sheet is currently active, driven by [HomeViewModel.activeSheet]. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSheetsHost(viewModel: HomeViewModel) {
    val sheetState = rememberModalBottomSheetState()
    val dismiss = { viewModel.closeSheet() }

    when (viewModel.activeSheet) {
        ActiveSheet.NONE -> Unit

        ActiveSheet.MEALS -> LogMealsSheet(viewModel, sheetState, dismiss)

        ActiveSheet.MOVEMENT -> {
            val (value, target) = viewModel.goalProgress(TrackingCategory.MOVEMENT)
            LogStepperSheet(
                title = "Log Movement",
                category = TrackingCategory.MOVEMENT,
                value = value,
                target = target,
                unit = "min",
                tip = dailyTips.getValue(TrackingCategory.MOVEMENT),
                onDecrement = { viewModel.adjustMovement(-5) },
                onIncrement = { viewModel.adjustMovement(5) },
                onQuickAdd = { viewModel.adjustMovement(dailyTips.getValue(TrackingCategory.MOVEMENT).quickAddAmount) },
                sheetState = sheetState,
                onDismiss = dismiss
            )
        }

        ActiveSheet.WATER -> {
            val (value, target) = viewModel.goalProgress(TrackingCategory.WATER)
            LogStepperSheet(
                title = "Log Water",
                category = TrackingCategory.WATER,
                value = value,
                target = target,
                unit = "glasses",
                tip = dailyTips.getValue(TrackingCategory.WATER),
                onDecrement = { viewModel.adjustWater(-1) },
                onIncrement = { viewModel.adjustWater(1) },
                onQuickAdd = { viewModel.adjustWater(dailyTips.getValue(TrackingCategory.WATER).quickAddAmount) },
                sheetState = sheetState,
                onDismiss = dismiss
            )
        }

        ActiveSheet.RELAXATION -> {
            val (value, target) = viewModel.goalProgress(TrackingCategory.RELAXATION)
            LogStepperSheet(
                title = "Log Relaxation",
                category = TrackingCategory.RELAXATION,
                value = value,
                target = target,
                unit = "min",
                tip = dailyTips.getValue(TrackingCategory.RELAXATION),
                onDecrement = { viewModel.adjustRelaxation(-5) },
                onIncrement = { viewModel.adjustRelaxation(5) },
                onQuickAdd = { viewModel.adjustRelaxation(dailyTips.getValue(TrackingCategory.RELAXATION).quickAddAmount) },
                sheetState = sheetState,
                onDismiss = dismiss
            )
        }

        ActiveSheet.SLEEP -> {
            val (value, target) = viewModel.goalProgress(TrackingCategory.SLEEP)
            LogStepperSheet(
                title = "Log Sleep",
                category = TrackingCategory.SLEEP,
                value = value,
                target = target,
                unit = "hrs",
                tip = dailyTips.getValue(TrackingCategory.SLEEP),
                onDecrement = { viewModel.adjustSleep(-1) },
                onIncrement = { viewModel.adjustSleep(1) },
                onQuickAdd = { viewModel.adjustSleep(dailyTips.getValue(TrackingCategory.SLEEP).quickAddAmount) },
                sheetState = sheetState,
                onDismiss = dismiss
            )
        }

        ActiveSheet.CYCLE -> LogCycleSheet(viewModel, sheetState, dismiss)
        ActiveSheet.SYMPTOMS -> TodaysSymptomsSheet(viewModel, sheetState, dismiss)
        ActiveSheet.EDIT_PROFILE -> EditProfileSheet(viewModel, sheetState, dismiss)
        ActiveSheet.REMINDER_TIME -> ReminderTimeSheet(viewModel, sheetState, dismiss)
        ActiveSheet.NOTIFICATION_PREVIEW -> LiveNotificationPreviewSheet(sheetState, dismiss)
    }
}
