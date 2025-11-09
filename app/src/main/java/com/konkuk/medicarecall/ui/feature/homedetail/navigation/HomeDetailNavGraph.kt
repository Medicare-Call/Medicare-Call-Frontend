package com.konkuk.medicarecall.ui.feature.homedetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.screen.GlucoseDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.meal.screen.MealDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.screen.MedicineDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.screen.SleepDetail
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.screen.StateHealthDetail
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.screen.StateMentalDetail
import com.konkuk.medicarecall.ui.navigation.Route

fun NavController.navigateToMealDetailScreen() {
    navigate(Route.MealDetailScreen)
}

fun NavController.navigateToMedicineDetailScreen() {
    navigate(Route.MedicineDetailScreen)
}

fun NavController.navigateToSleepDetail() {
    navigate(Route.SleepDetail)
}

fun NavController.navigateToHealthAnalysisDetail() {
    navigate(Route.HealthAnalysisDetail)
}

fun NavController.navigateToMentalAnalysisDetail() {
    navigate(Route.MentalAnalysisDetail)
}

fun NavController.navigateToGlucoseDetailScreen() {
    navigate(Route.GlucoseDetailScreen)
}

fun NavGraphBuilder.homeDetailNavGraph(
    popBackStack: () -> Unit,
) {
    // 홈 상세 화면_식사 화면
    composable<Route.MealDetailScreen> {
        MealDetailScreen(
            onBack = popBackStack,
        )
    }

    // 홈 상세 화면_복용 화면
    composable<Route.MedicineDetailScreen> {
        MedicineDetailScreen(onBack = popBackStack)
    }

    // 홈 상세 화면_수면 화면
    composable<Route.SleepDetail> {
        SleepDetail(onBack = popBackStack)
    }

    // 홈 상세 화면_건강 징후 화면
    composable<Route.HealthAnalysisDetail> {
        StateHealthDetail(onBack = popBackStack)
    }

    // 홈 상세 화면_심리 상태 화면
    composable<Route.MentalAnalysisDetail> {
        StateMentalDetail(onBack = popBackStack)
    }

    // 홈 상세 화면_혈당 화면
    composable<Route.GlucoseDetailScreen> {
        GlucoseDetailScreen(onBack = popBackStack)
    }
}
