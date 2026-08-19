package com.bloom.wellness.ui.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bloom.wellness.data.BloomPreferencesRepository
import com.bloom.wellness.data.model.ReminderPeriod
import com.bloom.wellness.data.model.TrackingCategory
import kotlinx.coroutines.launch

/** Holds the answers collected across all 4 onboarding steps until they're persisted at the end. */
class OnboardingViewModel(private val repository: BloomPreferencesRepository) : ViewModel() {

    var firstName by mutableStateOf("")
        private set

    var trackingEnabled by mutableStateOf(TrackingCategory.entries.associateWith { true })
        private set

    var selectedReminderPeriod by mutableStateOf<ReminderPeriod?>(null)
        private set

    var customHour by mutableStateOf(8)
        private set
    var customMinute by mutableStateOf(0)
        private set

    fun onFirstNameChange(name: String) {
        firstName = name
    }

    fun onTrackingToggle(category: TrackingCategory, enabled: Boolean) {
        trackingEnabled = trackingEnabled.toMutableMap().apply { put(category, enabled) }
    }

    fun onReminderPeriodSelected(period: ReminderPeriod) {
        selectedReminderPeriod = period
    }

    fun onCustomTimeChange(hour: Int, minute: Int) {
        customHour = hour
        customMinute = minute
        selectedReminderPeriod = ReminderPeriod.CUSTOM
    }

    /** Persists everything collected so far and marks onboarding complete. */
    fun finishOnboarding(skippedReminder: Boolean, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.setFirstName(firstName.trim())
            trackingEnabled.forEach { (category, enabled) -> repository.setTrackingEnabled(category, enabled) }
            if (!skippedReminder) {
                val period = selectedReminderPeriod ?: ReminderPeriod.MORNING
                if (period == ReminderPeriod.CUSTOM) {
                    repository.setReminderCustomTime(customHour, customMinute)
                } else {
                    repository.setReminderPeriod(period)
                }
                repository.setRemindersEnabled(true)
            } else {
                repository.setRemindersEnabled(false)
            }
            repository.setOnboardingCompleted(true)
            onDone()
        }
    }
}
