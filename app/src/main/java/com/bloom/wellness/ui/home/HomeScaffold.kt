package com.bloom.wellness.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bloom.wellness.ads.BloomBannerAd
import com.bloom.wellness.ui.BloomViewModelFactory
import com.bloom.wellness.ui.bloomRepository
import com.bloom.wellness.ui.components.BloomBottomNav
import com.bloom.wellness.ui.components.HomeTab
import com.bloom.wellness.ui.logsheets.HomeSheetsHost
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScaffold() {
    val repository = bloomRepository()
    val homeViewModel: HomeViewModel = viewModel(
        factory = BloomViewModelFactory(repository) { repo -> HomeViewModel(repo) }
    )
    var selectedTab by remember { mutableStateOf(HomeTab.DASHBOARD) }

    Scaffold(
        bottomBar = {
            Column {
                // Banner sits just above the nav bar, on the Home/Dashboard tab only, per the
                // suggested placement: content -> banner -> (never inside the scrolling content).
                if (selectedTab == HomeTab.DASHBOARD) {
                    BloomBannerAd()
                }
                BloomBottomNav(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                HomeTab.DASHBOARD -> DashboardScreen(homeViewModel)
                HomeTab.INSIGHTS -> InsightsScreen(homeViewModel)
                HomeTab.LEARN -> LearnScreen()
                HomeTab.SETTINGS -> SettingsScreen(homeViewModel)
            }
        }
    }

    HomeSheetsHost(homeViewModel)
}
