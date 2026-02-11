package com.konkuk.medicarecall.ui.feature.login.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.feature.login.calltime.screen.CallTimeScreen
import com.konkuk.medicarecall.ui.feature.login.elder.screen.LoginElderMedInfoScreen
import com.konkuk.medicarecall.ui.feature.login.elder.screen.LoginElderScreen
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderViewModel
import com.konkuk.medicarecall.ui.feature.login.myinfo.screen.LoginMyInfoScreen
import com.konkuk.medicarecall.ui.feature.login.myinfo.screen.LoginPhoneScreen
import com.konkuk.medicarecall.ui.feature.login.myinfo.screen.LoginStartScreen
import com.konkuk.medicarecall.ui.feature.login.myinfo.screen.LoginVerificationScreen
import com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel.LoginInfoViewModel
import com.konkuk.medicarecall.ui.feature.login.payment.screen.LoginFinishScreen
import com.konkuk.medicarecall.ui.navigation.Route

fun NavController.navigateToLoginStart() {
    navigate(Route.LoginStart)
}

fun NavController.navigateToLoginPhone() {
    navigate(Route.LoginPhone)
}

fun NavController.navigateToLoginVerification() {
    navigate(Route.LoginVerification)
}

fun NavController.navigateToLoginRegisterUserInfo(navOptions: NavOptions? = null) {
    navigate(Route.LoginRegisterUserInfo, navOptions)
}

fun NavController.navigateToLoginRegisterElder() {
    navigate(Route.LoginRegisterElder)
}

fun NavController.navigateToLoginRegisterElderHealth() {
    navigate(Route.LoginRegisterElderHealth)
}

fun NavController.navigateToLoginCareCallSetting(navOptions: NavOptions? = null) {
    navigate(Route.LoginCareCallSetting, navOptions)
}

fun NavController.navigateToLoginFinish() {
    navigate(Route.LoginFinish)
}

fun NavGraphBuilder.loginNavGraph(
    popBackStack: () -> Unit,
    navigateToMainAfterLogin: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToPhone: () -> Unit,
    navigateToVerification: () -> Unit,
    navigateToRegisterUserInfo: () -> Unit,
    navigateToRegisterElder: () -> Unit,
    navigateToRegisterElderHealth: () -> Unit,
    navigateToCareCallSetting: () -> Unit,
    navigateToCareCallSettingWithPopUpTo: () -> Unit,
    navigateToFinish: () -> Unit,
    getSharedLoginInfoViewModel: @Composable (NavBackStackEntry) -> LoginInfoViewModel,
    getSharedLoginElderViewModel: @Composable (NavBackStackEntry) -> LoginElderViewModel,
) {
    composable<Route.LoginStart> { backStackEntry ->
        val loginInfoViewModel = getSharedLoginInfoViewModel(backStackEntry)

        LoginStartScreen(
            navigateToPhone = navigateToPhone,
            navigateToRegisterElder = navigateToRegisterElder,
            navigateToCareCallSetting = navigateToCareCallSetting,
            navigateToPurchase = navigateToHome,
            navigateToHome = navigateToHome,
            loginInfoViewModel = loginInfoViewModel,
        )
    }
    composable<Route.LoginPhone> { backStackEntry ->
        val loginInfoViewModel = getSharedLoginInfoViewModel(backStackEntry)

        LoginPhoneScreen(
            onBack = popBackStack,
            navigateToVerification = navigateToVerification,
            viewModel = loginInfoViewModel,
        )
    }
    composable<Route.LoginVerification> { backStackEntry ->
        val loginInfoViewModel = getSharedLoginInfoViewModel(backStackEntry)

        LoginVerificationScreen(
            onBack = popBackStack,
            navigateToUserInfo = navigateToRegisterUserInfo,
            navigateToPhone = navigateToPhone,
            navigateToRegisterElder = navigateToRegisterElder,
            navigateToCareCallSetting = navigateToCareCallSetting,
            navigateToPurchase = navigateToHome,
            navigateToHome = navigateToHome,
            viewModel = loginInfoViewModel,
        )
    }
    composable<Route.LoginRegisterUserInfo> { backStackEntry ->
        val loginInfoViewModel = getSharedLoginInfoViewModel(backStackEntry)

        LoginMyInfoScreen(
            onBack = popBackStack,
            navigateToRegisterElder = navigateToRegisterElder,
            viewModel = loginInfoViewModel,
        )
    }
    composable<Route.LoginRegisterElder> { backStackEntry ->
        val loginElderViewModel = getSharedLoginElderViewModel(backStackEntry)

        LoginElderScreen(
            onBack = popBackStack,
            navigateToRegisterElderHealth = navigateToRegisterElderHealth,
            viewModel = loginElderViewModel,
        )
    }
    composable<Route.LoginRegisterElderHealth> { backStackEntry ->
        val loginElderViewModel = getSharedLoginElderViewModel(backStackEntry)

        LoginElderMedInfoScreen(
            onBack = popBackStack,
            navigateToCareCallSetting = navigateToCareCallSettingWithPopUpTo,
            viewModel = loginElderViewModel,
        )
    }

    composable<Route.LoginCareCallSetting> {
        CallTimeScreen(
            onBack = popBackStack,
            navigateToPayment = navigateToFinish,
        )
    }

    composable<Route.LoginFinish> {
        LoginFinishScreen(
            navigateToMain = navigateToMainAfterLogin,
        )
    }
}
