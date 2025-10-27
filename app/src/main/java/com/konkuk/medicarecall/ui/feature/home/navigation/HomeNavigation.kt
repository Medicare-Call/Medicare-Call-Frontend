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
    navigateToMealDetail: () -> Unit,
    navigateToMedicationDetail: () -> Unit,
    navigateToSleepDetail: () -> Unit,
    navigateToHealthAnalysisDetail: () -> Unit,
    navigateToMentalAnalysisDetail: () -> Unit,
    navigateToGlucoseDetail: () -> Unit,
) {
    composable<MainTabRoute.Home> { backStackEntry ->
        HomeScreen(
            navigateToMealDetail = navigateToMealDetail,
            navigateToMedicationDetail = navigateToMedicationDetail,
            navigateToSleepDetail = navigateToSleepDetail,
            navigateToHealthAnalysisDetail = navigateToHealthAnalysisDetail,
            navigateToMentalAnalysisDetail = navigateToMentalAnalysisDetail,
            navigateToGlucoseDetail = navigateToGlucoseDetail,
            mainBackStackEntry = backStackEntry,
        )
    }
}
