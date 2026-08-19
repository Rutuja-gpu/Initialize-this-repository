package com.bloom.wellness.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

/** One trackable daily category, shown in onboarding step 3, Settings, and the dashboard goal list. */
enum class TrackingCategory(
    val storeKey: String,
    val title: String,
    val onboardingSubtitle: String,
    val icon: ImageVector,
    val defaultTarget: Int,
    val unitLabel: String
) {
    MEALS(
        "meals", "Meals", "Steadier blood sugar helps keep PCOS symptoms in check.",
        Icons.Outlined.Restaurant, 3, "meals"
    ),
    MOVEMENT(
        "movement", "Movement", "Supports insulin sensitivity, a key lever in PCOS.",
        Icons.Outlined.DirectionsWalk, 30, "min"
    ),
    WATER(
        "water", "Water", "Helps with energy and bloating.",
        Icons.Outlined.LocalDrink, 8, "glasses"
    ),
    RELAXATION(
        "relaxation", "Relaxation", "Lower stress means lower cortisol, which can worsen symptoms.",
        Icons.Outlined.Spa, 15, "min"
    ),
    SLEEP(
        "sleep", "Sleep", "Consistent sleep helps balance hormones over time.",
        Icons.Outlined.Bedtime, 8, "hrs"
    ),
    CYCLE(
        "cycle", "Cycle", "Spot your personal pattern and predict your next period.",
        Icons.Outlined.WaterDrop, 0, "period dates"
    ),
    SYMPTOM_CHECKIN(
        "symptom_checkin", "Symptom check-in", "Track how you feel day to day to notice patterns over time.",
        Icons.Outlined.SentimentSatisfied, 0, "bloating, skin, mood, sleep quality"
    );

    companion object {
        /** Categories with a numeric daily goal shown in the dashboard's "Goal Progress" list. */
        val goalCategories = listOf(MEALS, MOVEMENT, WATER, RELAXATION, SLEEP)
    }
}

enum class JourneyStage(val storeKey: String, val title: String, val subtitle: String) {
    DIAGNOSED("diagnosed", "Diagnosed", "I have a confirmed PCOS diagnosis"),
    SUSPECTED("suspected", "Suspected", "A doctor mentioned it, not confirmed"),
    EXPLORING("exploring", "Exploring", "I think I might have symptoms"),
    NOT_SURE("not_sure", "Not sure yet", "I just want to learn more")
}

enum class ReminderPeriod(val storeKey: String, val title: String, val subtitle: String, val hour: Int, val minute: Int) {
    MORNING("morning", "Morning", "7:30 AM — start the day on track", 7, 30),
    AFTERNOON("afternoon", "Afternoon", "1:00 PM — a midday check-in", 13, 0),
    EVENING("evening", "Evening", "8:00 PM — wind down and reflect", 20, 0),
    CUSTOM("custom", "Custom", "Pick your own time", 8, 0)
}

enum class MealSlot(val label: String) { BREAKFAST("Breakfast"), LUNCH("Lunch"), DINNER("Dinner") }

enum class MealRating(val label: String) { LIGHT("Light"), BALANCED("Balanced"), INDULGENT("Indulgent"), SKIPPED("Skipped") }

/** A four-option severity scale used by each symptom row in the "Today's symptoms" sheet. */
data class SymptomScale(val key: String, val title: String, val options: List<String>)

val symptomScales = listOf(
    SymptomScale("bloating", "Bloating", listOf("None", "Mild", "Moderate", "Severe")),
    SymptomScale("skin", "Skin & acne", listOf("Clear", "Mild", "Moderate", "Flare-up")),
    SymptomScale("mood", "Mood", listOf("Great", "Okay", "Low", "Very low")),
    SymptomScale("sleep_quality", "Sleep quality", listOf("Great", "Okay", "Poor", "Very poor"))
)

/** Contextual nudge copy shown both in the dashboard "Today's tip" card and inline in each log sheet. */
data class DailyTip(val label: String, val title: String, val body: String, val quickAddAmount: Int, val quickAddUnit: String)

val dailyTips: Map<TrackingCategory, DailyTip> = mapOf(
    TrackingCategory.SLEEP to DailyTip(
        "LOG LAST NIGHT'S SLEEP", "Log last night's sleep",
        "You haven't logged sleep yet. Consistent, close-to-8-hr nights can help keep your hormones more balanced — avoiding phones and screens before bed makes it easier to fall asleep.",
        1, "hrs"
    ),
    TrackingCategory.MOVEMENT to DailyTip(
        "GET MOVING TODAY", "Get moving today",
        "No movement logged today yet. Try a brisk 30-minute walk, a swim, or some light strength training — these are especially good for insulin sensitivity in PCOS.",
        10, "min"
    ),
    TrackingCategory.WATER to DailyTip(
        "START HYDRATING", "Start hydrating",
        "No water logged today yet. Try starting hydrating first thing in the morning.",
        1, "glasses"
    ),
    TrackingCategory.RELAXATION to DailyTip(
        "MAKE TIME TO UNWIND", "Make time to unwind",
        "No relaxation time logged yet. Even 5–10 minutes of deep breathing, meditation, or gentle yoga can help lower stress hormones.",
        5, "min"
    )
)

data class LearnTopic(val question: String)
data class LearnSection(val title: String, val topics: List<LearnTopic>)

val learnSections = listOf(
    LearnSection(
        "Diagnosis & basics", listOf(
            LearnTopic("What exactly is PCOS?"),
            LearnTopic("What does PCOS stand for — and do I actually have cysts on my ovaries?"),
            LearnTopic("What causes PCOS?"),
            LearnTopic("How is PCOS actually diagnosed?"),
            LearnTopic("Is PCOS the same for everyone?")
        )
    ),
    LearnSection(
        "Symptoms & body", listOf(
            LearnTopic("Why are my periods irregular?"),
            LearnTopic("Why does weight feel harder to manage?"),
            LearnTopic("What's the deal with insulin resistance?"),
            LearnTopic("Is the acne and hair growth from PCOS treatable?")
        )
    ),
    LearnSection(
        "Managing it day to day", listOf(
            LearnTopic("Can food really make a difference?"),
            LearnTopic("What foods should I eat — and which should I limit?"),
            LearnTopic("How can I manage insulin resistance day to day?"),
            LearnTopic("Will I need medication forever?"),
            LearnTopic("How does stress fit into all this?")
        )
    ),
    LearnSection(
        "Looking ahead", listOf(
            LearnTopic("Does PCOS affect fertility?"),
            LearnTopic("What happens if PCOS is left unmanaged?")
        )
    )
)
