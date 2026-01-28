package com.konkuk.medicarecall.ui.feature.statistics.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.feature.statistics.screen.StatisticsScreen
import com.konkuk.medicarecall.ui.navigation.MainTabRoute

fun NavController.navigateToStatistics(navOptions: NavOptions) {
    navigate(MainTabRoute.WeeklyStatistics, navOptions)
}

fun NavGraphBuilder.statisticsNavGraph(
    navController: NavHostController,
    navigateToAlarm: () -> Unit,
    getBackStackHomeViewModel: @Composable (NavBackStackEntry) -> HomeViewModel,
) {
    composable<MainTabRoute.WeeklyStatistics> { backStackEntry ->
        StatisticsScreen(
            navController = navController,
            homeViewModel = getBackStackHomeViewModel(backStackEntry),
            navigateToAlarm = navigateToAlarm,
        )
    }
}
