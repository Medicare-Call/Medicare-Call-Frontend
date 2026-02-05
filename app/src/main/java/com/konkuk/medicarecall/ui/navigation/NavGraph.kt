package com.konkuk.medicarecall.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.konkuk.medicarecall.ui.common.extension.sharedViewModel
import com.konkuk.medicarecall.ui.feature.alarm.navigation.alarmNavGraph
import com.konkuk.medicarecall.ui.feature.home.navigation.homeNavGraph
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.homeDetailNavGraph
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderViewModel
import com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel.LoginInfoViewModel
import com.konkuk.medicarecall.ui.feature.login.navigation.loginNavGraph
import com.konkuk.medicarecall.ui.feature.settings.navigation.settingNavGraph
import com.konkuk.medicarecall.ui.feature.splash.screen.SplashScreen
import com.konkuk.medicarecall.ui.feature.statistics.navigation.statisticsNavGraph

// ---- 헬퍼: 로그인 성공 후 인증 그래프 제거하고 main으로 ---
fun NavHostController.navigateToMainAfterLogin() {
    navigate(MainTabRoute.Home) {
        popUpTo(0) { inclusive = true }
    }
}

@Composable
fun NavGraph(
    navigator: MainNavigator,
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
            popBackStack = navigator::popBackStack,
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
            popBackStack = navigator::popBackStack,
        )

        // 통계 네비게이션
        statisticsNavGraph(
            navController = navController,
            navigateToAlarm = navigator::navigateToAlarm,
            getBackStackHomeViewModel = { backStackEntry -> backStackEntry.sharedViewModel<HomeViewModel, MainTabRoute.Home>(navController) },
        )

        // 설정 네비게이션
        settingNavGraph(
            popBackStack = navigator::popBackStack,
            navigateToElderPersonalInfo = navigator::navigateToElderPersonalInfo,
            navigateToElderPersonalDetail = navigator::navigateToElderPersonalDetail,
            navigateToElderHealthInfo = navigator::navigateToHealthInfo,
            navigateToHealthDetail = navigator::navigateToHealthDetail,
            navigateToNotificationSetting = navigator::navigateToNotificationSetting,
            navigateToSubscribeInfo = navigator::navigateToSubscribeInfo,
            navigateToSubscribeDetail = navigator::navigateToSubscribeDetail,
            navigateToNotice = navigator::navigateToNotice,
            navigateToNoticeDetail = navigator::navigateToNoticeDetail,
            navigateToServiceCenter = navigator::navigateToServiceCenter,
            navigateToUserInfo = navigator::navigateToUserInfo,
            navigateToUserInfoSetting = navigator::navigateToUserInfoSetting,
            navigateToLoginAfterLogout = navigator::navigateToLoginAfterLogout,
            navController = navController,
        )

        // 로그인 네비게이션
        loginNavGraph(
            popBackStack = navigator::popBackStack,
            navigateToMainAfterLogin = { navController.navigateToMainAfterLogin() },
            navigateToHome = { navController.navigateToMainAfterLogin() },
            navigateToPhone = navigator::navigateToLoginPhone,
            navigateToVerification = navigator::navigateToLoginVerification,
            navigateToRegisterUserInfo = navigator::navigateToLoginRegisterUserInfo,
            navigateToRegisterElder = navigator::navigateToLoginRegisterElder,
            navigateToRegisterElderHealth = navigator::navigateToLoginRegisterElderHealth,
            navigateToCareCallSetting = { navController.navigate(Route.LoginCareCallSetting) },
            navigateToCareCallSettingWithPopUpTo = navigator::navigateToLoginCareCallSetting,
            navigateToFinish = navigator::navigateToLoginFinish,
            getSharedLoginInfoViewModel = { backStackEntry ->
                backStackEntry.sharedViewModel<LoginInfoViewModel, Route.LoginStart>(navController)
            },
            getSharedLoginElderViewModel = { backStackEntry ->
                backStackEntry.sharedViewModel<LoginElderViewModel, Route.LoginRegisterElder>(navController)
            },
        )
    }
}
