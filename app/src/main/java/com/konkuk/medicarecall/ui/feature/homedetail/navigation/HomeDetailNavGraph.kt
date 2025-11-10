package com.konkuk.medicarecall.ui.feature.homedetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.screen.GlucoseDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.meal.screen.MealDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.screen.MedicineDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.screen.SleepDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.screen.StateHealthDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.screen.StateMentalDetailScreen
import com.konkuk.medicarecall.ui.navigation.Route

fun NavController.navigateToMealDetailScreen() {
    navigate(Route.MealDetail)
}

fun NavController.navigateToMedicineDetailScreen() {
    navigate(Route.MedicineDetail)
}

fun NavController.navigateToSleepDetailScreen() {
    navigate(Route.SleepDetail)
}

fun NavController.navigateToStateHealthDetailScreen() {
    navigate(Route.StateHealthDetail)
}

fun NavController.navigateToStateMentalDetailScreen() {
    navigate(Route.StateMentalDetail)
}

fun NavController.navigateToGlucoseDetailScreen() {
    navigate(Route.GlucoseDetail)
}

fun NavGraphBuilder.homeDetailNavGraph(
    popBackStack: () -> Unit,
) {
    // 홈 상세 화면_식사 화면
    composable<Route.MealDetail> {
        MealDetailScreen(
            onBack = popBackStack,
        )
    }

    // 홈 상세 화면_복용 화면
    composable<Route.MedicineDetail> {
        MedicineDetailScreen(onBack = popBackStack)
    }

    // 홈 상세 화면_수면 화면
    composable<Route.SleepDetail> {
        SleepDetailScreen(onBack = popBackStack)
    }

    // 홈 상세 화면_건강 징후 화면
    composable<Route.StateHealthDetail> {
        StateHealthDetailScreen(onBack = popBackStack)
    }

    // 홈 상세 화면_심리 상태 화면
    composable<Route.StateMentalDetail> {
        StateMentalDetailScreen(onBack = popBackStack)
    }

    // 홈 상세 화면_혈당 화면
    composable<Route.GlucoseDetail> {
        GlucoseDetailScreen(onBack = popBackStack)
    }
}
