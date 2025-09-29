package com.konkuk.medicarecall.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.konkuk.medicarecall.ui.feature.home.navigation.navigateToHome
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToGlucoseDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToHealthAnalysisDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToMealDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToMedicationDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToMentalAnalysisDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToSleepDetail
import com.konkuk.medicarecall.ui.feature.statistics.navigation.navigateToStatistics
import com.konkuk.medicarecall.ui.navigation.component.MainTab

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination
    val startDestination = Route.Splash

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    // 메인 탭 이동 함수
    fun navigateToMainTab(tab: MainTab) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions)
            MainTab.WEEKLY_STATISTICS -> navController.navigateToStatistics(navOptions)
            MainTab.SETTINGS -> navController.navigate(MainTabRoute.Settings, navOptions)
        }
    }

    // 뒤로 가기 함수
    fun popBackStack() {
        navController.popBackStack()
    }

    /* 홈 상세 화면 */
    fun navigateToMealDetail() {
        navController.navigateToMealDetail()
    }

    fun navigateToMedicationDetail() {
        navController.navigateToMedicationDetail()
    }

    fun navigateToSleepDetail() {
        navController.navigateToSleepDetail()
    }

    fun navigateToHealthAnalysisDetail() {
        navController.navigateToHealthAnalysisDetail()
    }

    fun navigateToMentalAnalysisDetail() {
        navController.navigateToMentalAnalysisDetail()
    }

    fun navigateToGlucoseDetail() {
        navController.navigateToGlucoseDetail()
    }

    // 현재 화면이 BottomBar를 보여줘야 하는지 여부
    @Composable
    fun shouldShowBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
