package com.konkuk.medicarecall.ui.navigation.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.konkuk.medicarecall.R
import com.konkuk.medicarecall.ui.navigation.MainTabRoute
import com.konkuk.medicarecall.ui.navigation.Route

enum class MainTab(
    val label: String,
    val route: MainTabRoute,
    val description: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
) {
    HOME(
        label = "하루 요약",
        route = MainTabRoute.Home,
        description = "Home Icon",
        selectedIcon = R.drawable.ic_home_selected,
        unselectedIcon = R.drawable.ic_home_unselected,
    ),
    WEEKLY_STATISTICS(
        label = "주간 통계",
        route = MainTabRoute.WeeklyStatistics,
        description = "Statistics Icon",
        selectedIcon = R.drawable.ic_statistics_selected,
        unselectedIcon = R.drawable.ic_statistics_unselected,
    ),
    SETTINGS(
        label = "설정",
        route = MainTabRoute.Settings,
        description = "Settings Icon",
        selectedIcon = R.drawable.ic_settings_selected,
        unselectedIcon = R.drawable.ic_settings_unselected,
    ),
    ;

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return MainTab.entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return MainTab.entries.map { it.route }.any { predicate(it) }
        }
    }
}
