package com.konkuk.medicarecall.ui.feature.alarm.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.feature.alarm.screen.AlarmScreen
import com.konkuk.medicarecall.ui.navigation.Route

fun NavController.navigateToAlarm() {
    navigate(Route.Alarm)
}

fun NavGraphBuilder.alarmNavGraph(
    popBackStack: () -> Unit,
) {
    composable<Route.Alarm> {
        AlarmScreen(
            onBack = popBackStack,
        )
    }
}
