package com.konkuk.medicarecall.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.ui.feature.alarm.navigation.alarmNavGraph
import com.konkuk.medicarecall.ui.feature.home.navigation.homeNavGraph
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.homeDetailNavGraph
import com.konkuk.medicarecall.ui.feature.statistics.navigation.statisticsNavGraph
import com.konkuk.medicarecall.ui.feature.settings.navigation.settingNavGraph
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
import com.konkuk.medicarecall.ui.feature.splash.screen.SplashScreen
import kotlin.reflect.typeOf

// ---- 헬퍼: 로그인 성공 후 인증 그래프 제거하고 main으로 ---
fun NavHostController.navigateToMainAfterLogin() {
    navigate(MainTabRoute.Home) {
        popUpTo(0) { inclusive = true }
    }
}

@Composable
fun NavGraph(
    navigator: MainNavigator,
    loginViewModel: LoginViewModel,
    loginElderViewModel: LoginElderViewModel,
    modifier: Modifier = Modifier,
) {
    val navController = navigator.navController

    NavHost(
        navController = navController,
        startDestination = navigator.startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier,
    ) {
        composable<Route.Splash> {
            SplashScreen(
                navigateToLogin = {
                    navController.navigate(Route.LoginStart) {
                        popUpTo(Route.Splash) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                navigateToStart = { navController.navigate(Route.LoginStart) },
                navigateToRegisterElder = { navController.navigate(Route.LoginRegisterElder) },
                navigateToCareCallSetting = { navController.navigate(Route.LoginCareCallSetting) },
                navigateToPurchase = { navController.navigateToMainAfterLogin() },
                navigateToHome = {
                    navController.navigateToMainAfterLogin()
                },
            )
        }

        // 알림 네비게이션
        alarmNavGraph(
            popBackStack = { navController.popBackStack() },
        )

        // 홈
//        composable<MainTabRoute.Home> { backStackEntry ->
//            HomeScreen(
//                navigateToMealDetailScreen = { navController.navigate(Route.MealDetailScreen) },
//                navigateToMedicineDetailScreen = { navController.navigate(Route.MedicineDetailScreen) },
//                navigateToSleepDetailScreen = { navController.navigate(Route.SleepDetailScreen) },
//                navigateToStateHealthDetailScreen = { navController.navigate(Route.StateHealthDetailScreen) },
//                navigateToStateMentalDetailScreen = { navController.navigate(Route.StateMentalDetailScreen) },
//                navigateToGlucoseDetailScreen = { navController.navigate(Route.GlucoseDetailScreen) },
//            )
//        }
        homeNavGraph(
            navigateToMealDetailScreen = navigator::navigateToMealDetailScreen,
            navigateToMedicineDetailScreen = navigator::navigateToMedicineDetailScreen,
            navigateToSleepDetailScreen = navigator::navigateToSleepDetailScreen,
            navigateToStateHealthDetailScreen = navigator::navigateToStateHealthDetailScreen,
            navigateToStateMentalDetailScreen = navigator::navigateToStateMentalDetailScreen,
            navigateToGlucoseDetailScreen = navigator::navigateToGlucoseDetailScreen,
        )

        // 홈 상세 네비게이션
        homeDetailNavGraph(
            popBackStack = { navController.popBackStack() },
        )

        // 통계 네비게이션
        statisticsNavGraph(
            navController = navController,
            navigateToAlarm = { navController.navigate(Route.Alarm) },
        )

        // 설정 네비게이션
        settingNavGraph(
            popBackStack = { navController.popBackStack() },
            navigateToElderPersonalInfo = { navController.navigate(Route.ElderPersonalInfo) },
            navigateToElderPersonalDetail = { elderInfo -> navController.navigate(Route.ElderPersonalDetail(elderInfo)) },
            navigateToElderHealthInfo = { navController.navigate(Route.ElderHealthInfo) },
            navigateToHealthDetail = { healthInfo -> navController.navigate(Route.ElderHealthDetail(healthInfo)) },
            navigateToNotificationSetting = { myInfo -> navController.navigate(Route.NotificationSetting(myInfo)) },
            navigateToSubscribeInfo = { navController.navigate(Route.SubscribeInfo) },
            navigateToSubscribeDetail = { subscription -> navController.navigate(Route.SubscribeDetail(subscription)) },
            navigateToNotice = { navController.navigate(Route.Notice) },
            navigateToNoticeDetail = { notice -> navController.navigate(Route.NoticeDetail(notice)) },
            navigateToServiceCenter = { navController.navigate(Route.ServiceCenter) },
            navigateToUserInfo = { navController.navigate(Route.UserInfo) },
            navigateToUserInfoSetting = { myInfo -> navController.navigate(Route.UserInfoSetting(myInfo)) },
            navigateToLoginAfterLogout = {
                navController.navigate(Route.LoginStart) {
                    popUpTo(MainTabRoute.Home) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            navController = navController,
        )

        // 로그인 내비게이션
        composable<Route.LoginStart> {
            LoginStartScreen(
                navigateToPhone = { navController.navigate(Route.LoginPhone) },
                navigateToRegisterElder = { navController.navigate(Route.LoginRegisterElder) },
                navigateToCareCallSetting = { navController.navigate(Route.LoginCareCallSetting) },
                navigateToPurchase = { navController.navigateToMainAfterLogin() },
                navigateToHome = {
                    navController.navigateToMainAfterLogin()
                },
                loginViewModel = loginViewModel,
            )
        }
        composable<Route.LoginPhone> {
            LoginPhoneScreen(
                onBack = { navController.popBackStack() },
                navigateToVerification = { navController.navigate(Route.LoginVerification) },
                loginViewModel = loginViewModel,
            )
        }
        composable<Route.LoginVerification> {
            LoginVerificationScreen(
                onBack = { navController.popBackStack() },
                navigateToUserInfo = {
                    navController.navigate(Route.LoginRegisterUserInfo) {
                        popUpTo(Route.LoginVerification) { inclusive = true }
                    }
                },
                navigateToPhone = { navController.navigate(Route.LoginPhone) },
                navigateToRegisterElder = { navController.navigate(Route.LoginRegisterElder) },
                navigateToCareCallSetting = { navController.navigate(Route.LoginCareCallSetting) },
                navigateToPurchase = { navController.navigateToMainAfterLogin() },
                navigateToHome = {
                    navController.navigateToMainAfterLogin()
                },
                loginViewModel = loginViewModel,
            )
        }
        composable<Route.LoginRegisterUserInfo> {
            LoginMyInfoScreen(
                onBack = { navController.popBackStack() },
                navigateToRegisterElder = {
                    navController.navigate(Route.LoginRegisterElder) {
                        popUpTo(Route.LoginStart)
                    }
                },
                loginViewModel = loginViewModel,
            )
        }
        composable<Route.LoginRegisterElder> {
            LoginElderScreen(
                onBack = { navController.popBackStack() },
                navigateToRegisterElderHealth = {
                    navController.navigate(Route.LoginRegisterElderHealth)
                },
                loginElderViewModel = loginElderViewModel,
            )
        }
        composable<Route.LoginRegisterElderHealth> {
            LoginElderMedInfoScreen(
                onBack = { navController.popBackStack() },
                navigateToCareCallSetting = {
                    navController.navigate(Route.LoginCareCallSetting) {
                        popUpTo(Route.LoginRegisterElder) {
                            inclusive = true
                        }
                    }
                },
                loginElderViewModel = loginElderViewModel,
            )
        }

        composable<Route.LoginCareCallSetting> {
            CallTimeScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToPayment = {
                    navController.navigate(Route.LoginFinish) {
                        popUpTo(Route.LoginNaverPayView) { inclusive = true }
                    }
                },
            )
        }

        composable<Route.LoginPurchase> {
            PaymentScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToNaverPay = {
                    navController.navigate(Route.LoginNaverPayView)
                },
            )
        }

        composable<Route.LoginNaverPayView> {
            NaverPayWebViewScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToFinish = {
                    navController.navigate(Route.LoginFinish) {
                        popUpTo(Route.LoginNaverPayView) { inclusive = true }
                    }
                },
            )
        }

        composable<Route.LoginFinish> {
            LoginFinishScreen(
                navigateToMain = {
                    navController.navigateToMainAfterLogin()
                },
            )
        }

//        loginNavGraph(
//            popBackStack = navigator::popBackStack,
//            navigateToHome = navigator::navigateToHome,
//            navigateToPhone = navigator::navigateToLoginPhone,
//            navigateToVerification = navigator::navigateToLoginVerification,
//            navigateTpRegisterUserInfo = navigator::navigateToLoginRegisterUserInfo,
//            navigateToRegisterElder = navigator::navigateToLoginRegisterElder,
//            navigateToRegisterElderHealth = navigator::navigateToLoginRegisterElderHealth,
//            navigateToCareCallSetting = navigator::navigateToLoginCareCallSetting,
//            navigateToCareCallSettingWithPopUpTo = navigator::navigateToLoginCareCallSetting,
//            navigateToPurchase = navigator::navigateToLoginPurchase,
//            navigateToNaverPayView = navigator::navigateToLoginNaverPayView,
//            navigateToFinish = navigator::navigateToLoginFinish,
//            navigateToMainAfterLogin = navController::navigateToMainAfterLogin,
//            getBackStackLoginViewModel = { backStackEntry ->
//                backStackEntry
//                    .sharedViewModel<LoginViewModel, Route.LoginStart>(navController)
//            },
//            getBackStackLoginElderViewModel = { backStackEntry ->
//                backStackEntry
//                    .sharedViewModel<LoginElderViewModel, Route.LoginRegisterElder>(navController)
//            }
//        )
    }
}
