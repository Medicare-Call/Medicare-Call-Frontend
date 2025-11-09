package com.konkuk.medicarecall.ui.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.feature.home.screen.HomeScreen
import com.konkuk.medicarecall.ui.navigation.MainTabRoute

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    navigateToMealDetailScreen: () -> Unit,
    navigateToMedicineDetailScreen: () -> Unit,
    navigateToSleepDetailScreen: () -> Unit,
    navigateToHealthAnalysisDetail: () -> Unit,
    navigateToMentalAnalysisDetail: () -> Unit,
    navigateToGlucoseDetailScreen: () -> Unit,
) {
    composable<MainTabRoute.Home> { backStackEntry ->
        HomeScreen(
            navigateToMealDetailScreen = navigateToMealDetailScreen,
            navigateToMedicineDetailScreen = navigateToMedicineDetailScreen,
            navigateToSleepDetailScreen = navigateToSleepDetailScreen,
            navigateToHealthAnalysisDetail = navigateToHealthAnalysisDetail,
            navigateToMentalAnalysisDetail = navigateToMentalAnalysisDetail,
            navigateToGlucoseDetailScreen = navigateToGlucoseDetailScreen,
            mainBackStackEntry = backStackEntry,
        )
    }
}
