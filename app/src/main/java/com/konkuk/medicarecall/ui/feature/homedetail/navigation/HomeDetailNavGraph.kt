package com.konkuk.medicarecall.ui.feature.homedetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.screen.GlucoseDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.meal.screen.MealDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.screen.MedicineDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.screen.SleepDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.screen.StateHealthDetailScreen
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.screen.StateMentalDetailScreen
import com.konkuk.medicarecall.ui.navigation.Route

fun NavController.navigateToMealDetailScreen(elderId: Long) {
    navigate(Route.MealDetail(elderId))
}

fun NavController.navigateToMedicineDetailScreen(elderId: Long) {
    navigate(Route.MedicineDetail(elderId))
}

fun NavController.navigateToSleepDetailScreen(elderId: Long) {
    navigate(Route.SleepDetail(elderId))
}

fun NavController.navigateToStateHealthDetailScreen(elderId: Long) {
    navigate(Route.StateHealthDetail(elderId))
}

fun NavController.navigateToStateMentalDetailScreen(elderId: Long) {
    navigate(Route.StateMentalDetail(elderId))
}

fun NavController.navigateToGlucoseDetailScreen(elderId: Long) {
    navigate(Route.GlucoseDetail(elderId))
}

fun NavGraphBuilder.homeDetailNavGraph(
    popBackStack: () -> Unit,
) {
    // 홈 상세 화면_식사 화면
    composable<Route.MealDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.MealDetail>()
        MealDetailScreen(
            elderId = route.elderId,
            onBack = popBackStack,
        )
    }

    // 홈 상세 화면_복용 화면
    composable<Route.MedicineDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.MedicineDetail>()
        MedicineDetailScreen(
            elderId = route.elderId,
            onBack = popBackStack,
        )
    }

    // 홈 상세 화면_수면 화면
    composable<Route.SleepDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.SleepDetail>()
        SleepDetailScreen(
            onBack = popBackStack,
        )
    }

    // 홈 상세 화면_건강 징후 화면
    composable<Route.StateHealthDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.StateHealthDetail>()
        StateHealthDetailScreen(
            elderId = route.elderId,
            onBack = popBackStack,
        )
    }

    // 홈 상세 화면_심리 상태 화면
    composable<Route.StateMentalDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.StateMentalDetail>()
        StateMentalDetailScreen(
            elderId = route.elderId,
            onBack = popBackStack,
        )
    }

    // 홈 상세 화면_혈당 화면
    composable<Route.GlucoseDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.GlucoseDetail>()
        GlucoseDetailScreen(
            elderId = route.elderId,
            onBack = popBackStack,
        )
    }
}
