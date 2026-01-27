package com.konkuk.medicarecall.ui.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.common.extension.sharedViewModel
import com.konkuk.medicarecall.ui.feature.login.carecall.screen.CallTimeScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginMyInfoScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginPhoneScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginStartScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginVerificationScreen
import com.konkuk.medicarecall.ui.feature.login.info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.feature.login.payment.screen.LoginFinishScreen
import com.konkuk.medicarecall.ui.feature.login.payment.screen.NaverPayWebViewScreen
import com.konkuk.medicarecall.ui.feature.login.payment.screen.PaymentScreen
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

fun NavController.navigateToLoginPurchase() {
    navigate(Route.LoginPurchase)
}

fun NavController.navigateToLoginNaverPayView() {
    navigate(Route.LoginNaverPayView)
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
    navigateToPurchase: () -> Unit,
    navigateToNaverPayView: () -> Unit,
    navigateToFinish: () -> Unit,
) {
    composable<Route.LoginStart> { backStackEntry ->
        val loginViewModel: LoginViewModel = backStackEntry.sharedViewModel<LoginViewModel, Route.LoginStart>(navController)

        LoginStartScreen(
            navigateToPhone = navigateToPhone,
            navigateToRegisterElder = navigateToRegisterElder,
            navigateToCareCallSetting = navigateToCareCallSetting,
            navigateToPurchase = navigateToPurchase,
            navigateToHome = navigateToHome,
            loginViewModel = loginViewModel,
        )
    }
    composable<Route.LoginPhone> { backStackEntry ->
        val loginViewModel: LoginViewModel = backStackEntry.sharedViewModel<LoginViewModel, Route.LoginStart>(navController)

        LoginPhoneScreen(
            onBack = popBackStack,
            navigateToVerification = navigateToVerification,
            loginViewModel = loginViewModel,
        )
    }
    composable<Route.LoginVerification> { backStackEntry ->
        val loginViewModel: LoginViewModel = backStackEntry.sharedViewModel<LoginViewModel, Route.LoginStart>(navController)

        LoginVerificationScreen(
            onBack = popBackStack,
            navigateToUserInfo = navigateToRegisterUserInfo,
            navigateToPhone = navigateToPhone,
            navigateToRegisterElder = navigateToRegisterElder,
            navigateToCareCallSetting = navigateToCareCallSetting,
            navigateToPurchase = navigateToPurchase,
            navigateToHome = navigateToHome,
            loginViewModel = loginViewModel,
        )
    }
    composable<Route.LoginRegisterUserInfo> { backStackEntry ->
        val loginViewModel: LoginViewModel = backStackEntry.sharedViewModel<LoginViewModel, Route.LoginStart>(navController)

        LoginMyInfoScreen(
            onBack = popBackStack,
            navigateToRegisterElder = navigateToRegisterElder,
            loginViewModel = loginViewModel,
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
            navigateToPayment = navigateToPurchase,
        )
    }

    composable<Route.LoginPurchase> {
        PaymentScreen(
            onBack = popBackStack,
            navigateToNaverPay = navigateToNaverPayView,
        )
    }

    composable<Route.LoginNaverPayView> {
        NaverPayWebViewScreen(
            onBack = popBackStack,
            navigateToFinish = navigateToFinish,
        )
    }

    composable<Route.LoginFinish> {
        LoginFinishScreen(
            navigateToMain = navigateToMainAfterLogin,
        )
    }
}
