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
import com.konkuk.medicarecall.ui.feature.login.navigation.loginNavGraph
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

        // 로그인 네비게이션
        loginNavGraph(
            navController = navController,
            popBackStack = { navController.popBackStack() },
            navigateToMainAfterLogin = { navController.navigateToMainAfterLogin() },
            navigateToHome = { navController.navigateToMainAfterLogin() },
            navigateToPhone = { navController.navigate(Route.LoginPhone) },
            navigateToVerification = { navController.navigate(Route.LoginVerification) },
            navigateToRegisterUserInfo = {
                navController.navigate(Route.LoginRegisterUserInfo) {
                    popUpTo(Route.LoginVerification) { inclusive = true }
                }
            },
            navigateToRegisterElder = { navController.navigate(Route.LoginRegisterElder) },
            navigateToRegisterElderHealth = { navController.navigate(Route.LoginRegisterElderHealth) },
            navigateToCareCallSetting = { navController.navigate(Route.LoginCareCallSetting) },
            navigateToCareCallSettingWithPopUpTo = {
                navController.navigate(Route.LoginCareCallSetting) {
                    popUpTo(Route.LoginRegisterElder) { inclusive = true }
                }
            },
            navigateToPurchase = { navController.navigateToMainAfterLogin() },
            navigateToNaverPayView = { navController.navigate(Route.LoginNaverPayView) },
            navigateToFinish = {
                navController.navigate(Route.LoginFinish) {
                    popUpTo(Route.LoginNaverPayView) { inclusive = true }
                }
            },
        )
    }
}
