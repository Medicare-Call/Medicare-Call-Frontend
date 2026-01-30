package com.konkuk.medicarecall.ui.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.common.extension.sharedViewModel
import com.konkuk.medicarecall.ui.feature.login.calltime.screen.CallTimeScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginMyInfoScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginPhoneScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginStartScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginVerificationScreen
import com.konkuk.medicarecall.ui.feature.login.info.viewmodel.LoginInfoViewModel
import com.konkuk.medicarecall.ui.feature.login.payment.screen.LoginFinishScreen
import com.konkuk.medicarecall.ui.feature.login.senior.screen.LoginElderMedInfoScreen
import com.konkuk.medicarecall.ui.feature.login.senior.screen.LoginElderScreen
import com.konkuk.medicarecall.ui.feature.login.senior.viewmodel.LoginElderViewModel
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
    navController: NavHostController,
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
    // navigateToPurchase: () -> Unit,
    navigateToFinish: () -> Unit,
) {
    composable<Route.LoginStart> { backStackEntry ->
        val loginInfoViewModel: LoginInfoViewModel = backStackEntry.sharedViewModel<LoginInfoViewModel, Route.LoginStart>(navController)

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
        val loginInfoViewModel: LoginInfoViewModel = backStackEntry.sharedViewModel<LoginInfoViewModel, Route.LoginStart>(navController)

        LoginPhoneScreen(
            onBack = popBackStack,
            navigateToVerification = navigateToVerification,
            loginInfoViewModel = loginInfoViewModel,
        )
    }
    composable<Route.LoginVerification> { backStackEntry ->
        val loginInfoViewModel: LoginInfoViewModel = backStackEntry.sharedViewModel<LoginInfoViewModel, Route.LoginStart>(navController)

        LoginVerificationScreen(
            onBack = popBackStack,
            navigateToUserInfo = navigateToRegisterUserInfo,
            navigateToPhone = navigateToPhone,
            navigateToRegisterElder = navigateToRegisterElder,
            navigateToCareCallSetting = navigateToCareCallSetting,
            navigateToPurchase = navigateToHome,
            navigateToHome = navigateToHome,
            loginInfoViewModel = loginInfoViewModel,
        )
    }
    composable<Route.LoginRegisterUserInfo> { backStackEntry ->
        val loginInfoViewModel: LoginInfoViewModel = backStackEntry.sharedViewModel<LoginInfoViewModel, Route.LoginStart>(navController)

        LoginMyInfoScreen(
            onBack = popBackStack,
            navigateToRegisterElder = navigateToRegisterElder,
            loginInfoViewModel = loginInfoViewModel,
        )
    }
    composable<Route.LoginRegisterElder> { backStackEntry ->
        val loginElderViewModel: LoginElderViewModel = backStackEntry.sharedViewModel<LoginElderViewModel, Route.LoginRegisterElder>(navController)

        LoginElderScreen(
            onBack = popBackStack,
            navigateToRegisterElderHealth = navigateToRegisterElderHealth,
            loginElderViewModel = loginElderViewModel,
        )
    }
    composable<Route.LoginRegisterElderHealth> { backStackEntry ->
        val loginElderViewModel: LoginElderViewModel = backStackEntry.sharedViewModel<LoginElderViewModel, Route.LoginRegisterElder>(navController)

        LoginElderMedInfoScreen(
            onBack = popBackStack,
            navigateToCareCallSetting = navigateToCareCallSettingWithPopUpTo,
            loginElderViewModel = loginElderViewModel,
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
