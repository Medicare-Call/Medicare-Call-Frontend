package com.konkuk.medicarecall.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.home.screen.HomeScreen
import com.konkuk.medicarecall.ui.settings.screen.SettingsScreen
import com.konkuk.medicarecall.ui.statistics.screen.StatisticsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route, // 시작 화면
        modifier = modifier
    ) {
        // 홈
        composable(route = Route.Home.route) {
            HomeScreen()
        }
        // 통계
        composable(route = Route.Statistics.route) {
            StatisticsScreen()
        }
        // 설정
        composable(route = Route.Settings.route) {
            SettingsScreen()
        }
    }

}
