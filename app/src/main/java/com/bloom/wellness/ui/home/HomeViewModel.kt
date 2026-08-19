package com.bloom.wellness.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloom.wellness.data.BloomPreferencesRepository
import com.bloom.wellness.data.BloomProfile
import com.bloom.wellness.data.model.JourneyStage
import com.bloom.wellness.data.model.MealRating
import com.bloom.wellness.data.model.MealSlot
import com.bloom.wellness.data.model.ReminderPeriod
import com.bloom.wellness.data.model.TrackingCategory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class ActiveSheet { NONE, MEALS, MOVEMENT, WATER, RELAXATION, SLEEP, CYCLE, SYMPTOMS, EDIT_PROFILE, REMINDER_TIME, NOTIFICATION_PREVIEW }

/** Today's logged values. Intentionally in-memory only for this demo (see README limitations) — a
 * production build would persist one row per category per day in a local database. */
data class DailyLog(
    val meals: Map<MealSlot, MealRating?> = MealSlot.entries.associateWith { null },
    val movementMinutes: Int = 0,
    val waterGlasses: Int = 0,
    val relaxationMinutes: Int = 0,
    val sleepHours: Int = 0,
    val lastPeriodStart: LocalDate? = null,
    val symptoms: Map<String, String?> = emptyMap()
)

class HomeViewModel(private val repository: BloomPreferencesRepository) : ViewModel() {

    val profile = repository.profile.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), BloomProfile()
    )

    var dailyLog by mutableStateOf(DailyLog())
        private set

    var activeSheet by mutableStateOf(ActiveSheet.NONE)
        private set

    fun openSheet(sheet: ActiveSheet) {
        activeSheet = sheet
    }

    fun closeSheet() {
        activeSheet = ActiveSheet.NONE
    }

    fun logMeal(slot: MealSlot, rating: MealRating) {
        dailyLog = dailyLog.copy(meals = dailyLog.meals.toMutableMap().apply { put(slot, rating) })
    }

    fun adjustMovement(deltaMinutes: Int) {
        dailyLog = dailyLog.copy(movementMinutes = (dailyLog.movementMinutes + deltaMinutes).coerceAtLeast(0))
    }

    fun adjustWater(deltaGlasses: Int) {
        dailyLog = dailyLog.copy(waterGlasses = (dailyLog.waterGlasses + deltaGlasses).coerceAtLeast(0))
    }

    fun adjustRelaxation(deltaMinutes: Int) {
        dailyLog = dailyLog.copy(relaxationMinutes = (dailyLog.relaxationMinutes + deltaMinutes).coerceAtLeast(0))
    }

    fun adjustSleep(deltaHours: Int) {
        dailyLog = dailyLog.copy(sleepHours = (dailyLog.sleepHours + deltaHours).coerceAtLeast(0))
    }

    fun setLastPeriodStart(date: LocalDate) {
        dailyLog = dailyLog.copy(lastPeriodStart = date)
    }

    fun setSymptom(key: String, value: String) {
        dailyLog = dailyLog.copy(symptoms = dailyLog.symptoms.toMutableMap().apply { put(key, value) })
    }

    fun goalProgress(category: TrackingCategory): Pair<Int, Int> {
        val target = profile.value.goalTargets[category] ?: category.defaultTarget
        val current = when (category) {
            TrackingCategory.MEALS -> dailyLog.meals.values.count { it != null }
            TrackingCategory.MOVEMENT -> dailyLog.movementMinutes
            TrackingCategory.WATER -> dailyLog.waterGlasses
            TrackingCategory.RELAXATION -> dailyLog.relaxationMinutes
            TrackingCategory.SLEEP -> dailyLog.sleepHours
            else -> 0
        }
        return current to target
    }

    fun goalsCompletedCount(): Int = TrackingCategory.goalCategories.count { category ->
        val (current, target) = goalProgress(category)
        target > 0 && current >= target
    }

    fun setFirstName(name: String) = viewModelScope.launch { repository.setFirstName(name) }
    fun setJourneyStage(stage: JourneyStage) = viewModelScope.launch { repository.setJourneyStage(stage) }
    fun setTrackingEnabled(category: TrackingCategory, enabled: Boolean) =
        viewModelScope.launch { repository.setTrackingEnabled(category, enabled) }

    fun setGoalTarget(category: TrackingCategory, target: Int) =
        viewModelScope.launch { repository.setGoalTarget(category, target.coerceAtLeast(0)) }

    fun setRemindersEnabled(enabled: Boolean) = viewModelScope.launch { repository.setRemindersEnabled(enabled) }
    fun setReminderPeriod(period: ReminderPeriod) = viewModelScope.launch { repository.setReminderPeriod(period) }
    fun setReminderCustomTime(hour: Int, minute: Int) =
        viewModelScope.launch { repository.setReminderCustomTime(hour, minute) }
}
