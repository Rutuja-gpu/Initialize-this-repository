package com.bloom.wellness.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bloom.wellness.ui.bloomViewModel
import com.bloom.wellness.ui.home.HomeScaffold
import com.bloom.wellness.ui.onboarding.AboutYouScreen
import com.bloom.wellness.ui.onboarding.OnboardingViewModel
import com.bloom.wellness.ui.onboarding.ReminderScreen
import com.bloom.wellness.ui.onboarding.TrackingPreferencesScreen
import com.bloom.wellness.ui.onboarding.WelcomeScreen
import com.bloom.wellness.ui.splash.SplashScreen

@Composable
fun BloomNavHost() {
    val navController = rememberNavController()
    // One shared instance across all 4 onboarding steps, so answers survive back navigation.
    val onboardingViewModel = bloomViewModel { OnboardingViewModel(it) }

    fun goHome() {
        navController.navigate(Routes.HOME) {
            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Routes.ONBOARDING_WELCOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = { goHome() }
            )
        }

        composable(Routes.ONBOARDING_WELCOME) {
            WelcomeScreen(onGetStarted = { navController.navigate(Routes.ONBOARDING_ABOUT_YOU) })
        }

        composable(Routes.ONBOARDING_ABOUT_YOU) {
            AboutYouScreen(
                firstName = onboardingViewModel.firstName,
                onFirstNameChange = onboardingViewModel::onFirstNameChange,
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Routes.ONBOARDING_TRACKING) }
            )
        }

        composable(Routes.ONBOARDING_TRACKING) {
            TrackingPreferencesScreen(
                trackingEnabled = onboardingViewModel.trackingEnabled,
                onToggle = onboardingViewModel::onTrackingToggle,
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Routes.ONBOARDING_REMINDER) }
            )
        }

        composable(Routes.ONBOARDING_REMINDER) {
            ReminderScreen(
                selectedPeriod = onboardingViewModel.selectedReminderPeriod,
                customHour = onboardingViewModel.customHour,
                customMinute = onboardingViewModel.customMinute,
                onPeriodSelected = onboardingViewModel::onReminderPeriodSelected,
                onCustomTimeSelected = onboardingViewModel::onCustomTimeChange,
                onBack = { navController.popBackStack() },
                onEnterBloom = {
                    onboardingViewModel.finishOnboarding(skippedReminder = false) { goHome() }
                },
                onSkip = {
                    onboardingViewModel.finishOnboarding(skippedReminder = true) { goHome() }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScaffold()
        }
    }
}
