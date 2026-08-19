package com.bloom.wellness.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bloom.wellness.data.model.JourneyStage
import com.bloom.wellness.data.model.ReminderPeriod
import com.bloom.wellness.data.model.TrackingCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "bloom_prefs")

/** Immutable snapshot of everything collected during onboarding + edited later in Settings. */
data class BloomProfile(
    val onboardingCompleted: Boolean = false,
    val firstName: String = "",
    val journeyStage: JourneyStage? = null,
    val trackingEnabled: Map<TrackingCategory, Boolean> = TrackingCategory.entries.associateWith { true },
    val goalTargets: Map<TrackingCategory, Int> = TrackingCategory.entries.associateWith { it.defaultTarget },
    val remindersEnabled: Boolean = true,
    val reminderPeriod: ReminderPeriod = ReminderPeriod.MORNING,
    val reminderCustomHour: Int = 8,
    val reminderCustomMinute: Int = 0
)

/**
 * Persists onboarding completion + the user's profile/goal choices with DataStore so the
 * app can distinguish first launch ("Splash -> Onboarding -> Home") from a returning
 * launch ("Splash -> App Open Ad -> Home") per the app-open-ad requirements.
 */
class BloomPreferencesRepository(private val context: Context) {

    private object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val FIRST_NAME = stringPreferencesKey("first_name")
        val JOURNEY_STAGE = stringPreferencesKey("journey_stage")
        val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
        val REMINDER_PERIOD = stringPreferencesKey("reminder_period")
        val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
        fun tracking(category: TrackingCategory) = booleanPreferencesKey("track_${category.storeKey}")
        fun goal(category: TrackingCategory) = intPreferencesKey("goal_${category.storeKey}")
    }

    val profile: Flow<BloomProfile> = context.dataStore.data.map { prefs ->
        BloomProfile(
            onboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false,
            firstName = prefs[Keys.FIRST_NAME] ?: "",
            journeyStage = prefs[Keys.JOURNEY_STAGE]?.let { stored ->
                JourneyStage.entries.find { it.storeKey == stored }
            },
            trackingEnabled = TrackingCategory.entries.associateWith { category ->
                prefs[Keys.tracking(category)] ?: true
            },
            goalTargets = TrackingCategory.entries.associateWith { category ->
                prefs[Keys.goal(category)] ?: category.defaultTarget
            },
            remindersEnabled = prefs[Keys.REMINDERS_ENABLED] ?: true,
            reminderPeriod = prefs[Keys.REMINDER_PERIOD]?.let { stored ->
                ReminderPeriod.entries.find { it.storeKey == stored }
            } ?: ReminderPeriod.MORNING,
            reminderCustomHour = prefs[Keys.REMINDER_HOUR] ?: 8,
            reminderCustomMinute = prefs[Keys.REMINDER_MINUTE] ?: 0
        )
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[Keys.ONBOARDING_COMPLETED] = completed }
    }

    suspend fun setFirstName(name: String) {
        context.dataStore.edit { it[Keys.FIRST_NAME] = name }
    }

    suspend fun setJourneyStage(stage: JourneyStage) {
        context.dataStore.edit { it[Keys.JOURNEY_STAGE] = stage.storeKey }
    }

    suspend fun setTrackingEnabled(category: TrackingCategory, enabled: Boolean) {
        context.dataStore.edit { it[Keys.tracking(category)] = enabled }
    }

    suspend fun setGoalTarget(category: TrackingCategory, target: Int) {
        context.dataStore.edit { it[Keys.goal(category)] = target }
    }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.REMINDERS_ENABLED] = enabled }
    }

    suspend fun setReminderPeriod(period: ReminderPeriod) {
        context.dataStore.edit { it[Keys.REMINDER_PERIOD] = period.storeKey }
    }

    suspend fun setReminderCustomTime(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[Keys.REMINDER_PERIOD] = ReminderPeriod.CUSTOM.storeKey
            it[Keys.REMINDER_HOUR] = hour
            it[Keys.REMINDER_MINUTE] = minute
        }
    }
}
