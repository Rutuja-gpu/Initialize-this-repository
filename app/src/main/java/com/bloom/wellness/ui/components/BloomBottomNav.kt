package com.bloom.wellness.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTint
import com.bloom.wellness.ui.theme.BloomSurface
import com.bloom.wellness.ui.theme.BloomTextSecondary

enum class HomeTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DASHBOARD("dashboard", "Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
    INSIGHTS("insights", "Insights", Icons.Filled.Insights, Icons.Outlined.Insights),
    LEARN("learn", "Learn", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
    SETTINGS("settings", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}

@Composable
fun BloomBottomNav(selectedTab: HomeTab, onTabSelected: (HomeTab) -> Unit) {
    NavigationBar(containerColor = BloomSurface) {
        HomeTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(if (selected) tab.selectedIcon else tab.unselectedIcon, contentDescription = tab.label)
                },
                label = { Text(tab.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BloomMaroon,
                    selectedTextColor = BloomMaroon,
                    indicatorColor = BloomRoseTint,
                    unselectedIconColor = BloomTextSecondary,
                    unselectedTextColor = BloomTextSecondary
                )
            )
        }
    }
}
