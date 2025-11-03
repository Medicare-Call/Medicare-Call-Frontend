package com.konkuk.medicarecall.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.ui.feature.alarm.screen.AlarmScreen
import com.konkuk.medicarecall.ui.feature.home.navigation.homeNavGraph
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.screen.GlucoseDetail
import com.konkuk.medicarecall.ui.feature.homedetail.meal.screen.MealDetail
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.screen.MedicineDetail
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.screen.SleepDetail
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.screen.StateHealthDetail
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.screen.StateMentalDetail
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
import com.konkuk.medicarecall.ui.feature.settings.screen.AnnouncementDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.AnnouncementScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.HealthDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.HealthInfoScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.MyDataSettingScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.MyDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.ElderDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.ElderInfoScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.ServiceCenterScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingAlarmScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingSubscribeScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingsScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SubscribeDetailScreen
import com.konkuk.medicarecall.ui.feature.splash.screen.SplashScreen
import com.konkuk.medicarecall.ui.feature.statistics.screen.StatisticsScreen
import kotlin.reflect.typeOf

// ---- 헬퍼: 로그인 성공 후 인증 그래프 제거하고 main으로 ---
fun NavHostController.navigateToMainAfterLogin() {
    navigate(MainTabRoute.Home) {
        popUpTo(Route.LoginStart) { inclusive = true }
        launchSingleTop = true
        restoreState = true
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
                navigateToPhone = { navController.navigate(Route.LoginPhone) },
                navigateToRegisterElder = { navController.navigate(Route.LoginRegisterElder) },
                navigateToCareCallSetting = { navController.navigate(Route.LoginCareCallSetting) },
                navigateToPurchase = { navController.navigate(MainTabRoute.Home) },
                navigateToHome = {
                    navController.navigate(MainTabRoute.Home) {
                        popUpTo(Route.LoginStart) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        // 홈
//        composable<MainTabRoute.Home> { backStackEntry ->
//            HomeScreen(
//                navigateToMealDetail = { navController.navigate(Route.MealDetail) },
//                navigateToMedicationDetail = { navController.navigate(Route.MedicationDetail) },
//                navigateToSleepDetail = { navController.navigate(Route.SleepDetail) },
//                navigateToHealthAnalysisDetail = { navController.navigate(Route.HealthAnalysisDetail) },
//                navigateToMentalAnalysisDetail = { navController.navigate(Route.MentalAnalysisDetail) },
//                navigateToGlucoseDetail = { navController.navigate(Route.GlucoseDetail) },
//            )
//        }
        homeNavGraph(
            navigateToMealDetail = navigator::navigateToMealDetail,
            navigateToMedicationDetail = navigator::navigateToMedicationDetail,
            navigateToSleepDetail = navigator::navigateToSleepDetail,
            navigateToHealthAnalysisDetail = navigator::navigateToHealthAnalysisDetail,
            navigateToMentalAnalysisDetail = navigator::navigateToMentalAnalysisDetail,
            navigateToGlucoseDetail = navigator::navigateToGlucoseDetail,
        )

        // 홈 상세 화면_식사 화면
        composable<Route.MealDetail> {
            MealDetail(
                onBack = { navController.popBackStack() },
            )
        }

        // 홈 상세 화면_복용 화면
        composable<Route.MedicationDetail> {
            MedicineDetail(
                onBack = { navController.popBackStack() },
            )
        }

        // 홈 상세 화면_수면 화면
        composable<Route.SleepDetail> {
            SleepDetail(
                onBack = { navController.popBackStack() },
            )
        }

        // 홈 상세 화면_건강 징후 화면
        composable<Route.HealthAnalysisDetail> {
            StateHealthDetail(
                onBack = { navController.popBackStack() },
            )
        }

        // 홈 상세 화면_심리 상태 화면
        composable<Route.MentalAnalysisDetail> {
            StateMentalDetail(
                onBack = { navController.popBackStack() },
            )
        }

        // 홈 상세 화면_혈당 화면
        composable<Route.GlucoseDetail> {
            GlucoseDetail(
                onBack = { navController.popBackStack() },
            )
        }

        // 통계
        composable<MainTabRoute.WeeklyStatistics> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainTabRoute.Home)
            }
            val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

            StatisticsScreen(
                navController = navController,
                homeViewModel = homeViewModel,
            )
        }

//        statisticsNavGraph(
//            navController = navController,
//            getBackStackHomeViewModel = { backStackEntry ->
//                backStackEntry.sharedViewModel<HomeViewModel, MainTabRoute.Home>(navController)
//            },
//        )

        // 설정
        composable<MainTabRoute.Settings> {
            // TopLevelBackHandler(navController)
            SettingsScreen(
                navigateToUserInfo = {
                    navController.navigate(Route.UserInfo)
                },
                navigateToNotice = {
                    navController.navigate(Route.Notice)
                },
                navigateToCenter = {
                    navController.navigate(Route.ServiceCenter)
                },
                navigateToSubscribe = {
                    navController.navigate(Route.SubscribeInfo)
                },
                navigateToElderPersonalInfo = {
                    navController.navigate(Route.ElderPersonalInfo)
                },
                navigateToElderHealthInfo = {
                    navController.navigate(Route.ElderHealthInfo)
                },
                navigateToNotificationSetting = { myInfo ->
                    navController.navigate(Route.NotificationSetting(myInfo))
                },
            )
        }

        composable<Route.ElderPersonalInfo> {
            ElderInfoScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToElderDetail = { elderInfo ->
                    navController.navigate(Route.ElderPersonalDetail(elderInfo))
                },
            )
        }

        composable<Route.ElderPersonalDetail>(
            typeMap = mapOf(typeOf<EldersInfoResponseDto>() to EldersInfoResponseDtoType),
        ) { navBackstackEntry ->
            val elderInfo = navBackstackEntry.toRoute<Route.ElderPersonalDetail>().info
            ElderDetailScreen(
                onBack = { navController.popBackStack() },
                eldersInfoResponseDto = elderInfo,
                navController = navController,
            )
        }

        composable<Route.ElderHealthInfo> {
            HealthInfoScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToHealthDetail = { healthInfo ->
                    navController.navigate(Route.ElderHealthDetail(healthInfo))
                },
            )
        }

        composable<Route.ElderHealthDetail>(
            typeMap = mapOf(typeOf<EldersHealthResponseDto>() to EldersHealthResponseDtoType),
        ) { navBackstackEntry ->
            val healthInfo = navBackstackEntry.toRoute<Route.ElderHealthDetail>().health
            HealthDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                healthInfoResponseDto = healthInfo,
            )
        }

        composable<Route.NotificationSetting>(
            typeMap = mapOf(typeOf<MyInfoResponseDto>() to MyInfoResponseDtoType),
        ) { navBackStackEntry ->
            val myDataInfo = navBackStackEntry.toRoute<Route.NotificationSetting>().myInfo
            SettingAlarmScreen(
                myDataInfo = myDataInfo,
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        composable<Route.SubscribeInfo> {
            SettingSubscribeScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToSubscribeDetail = { subscription ->
                    navController.navigate(Route.SubscribeDetail(subscription))
                },
            )
        }

        composable<Route.SubscribeDetail>(
            typeMap = mapOf(typeOf<EldersSubscriptionResponseDto>() to EldersSubscriptionResponseDtoType),
        ) { navBackStackEntry ->
            val elderInfo = navBackStackEntry.toRoute<Route.SubscribeDetail>().subscription
            SubscribeDetailScreen(
                elderInfo = elderInfo,
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.Notice> {
            AnnouncementScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToNoticeDetail = { notice ->
                    navController.navigate(Route.NoticeDetail(notice))
                },
            )
        }

        composable<Route.NoticeDetail>(
            typeMap = mapOf(typeOf<NoticesResponseDto>() to NoticesResponseDtoType),
        ) { navBackStackEntry ->
            val noticeInfo = navBackStackEntry.toRoute<Route.NoticeDetail>().notice
            AnnouncementDetailScreen(
                noticeInfo = noticeInfo,
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.ServiceCenter> {
            ServiceCenterScreen(
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        composable<Route.UserInfo> {
            MyDataSettingScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToUserInfoSetting = { myInfo ->
                    navController.navigate(Route.UserInfoSetting(myInfo))
                },
                navigateToLoginAfterLogout = {
                    navController.navigate(Route.LoginStart) {
                        popUpTo(MainTabRoute.Home) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }

        composable<Route.UserInfoSetting>(
            typeMap = mapOf(typeOf<MyInfoResponseDto>() to MyInfoResponseDtoType),
        ) { navBackStackEntry ->
            val myDataInfo = navBackStackEntry.toRoute<Route.UserInfoSetting>().myInfo
            MyDetailScreen(
                myDataInfo = myDataInfo,
                onBack = {
                    navController.popBackStack()
                },
            )
        }

//        settingNavGraph(
//            popBackStack = navigator::popBackStack,
//            navigateToElderHealthInfo = navigator::navigateToElderPersonalInfo,
//            navigateToElderPersonalInfo = navigator::navigateToElderPersonalInfo,
//            navigateToElderPersonalDetail = navigator::navigateToElderPersonalDetail,
//            navigateToHealthDetail = navigator::navigateToHealthDetail,
//            navigateToNotificationSetting = navigator::navigateToNotificationSetting,
//            navigateToSubscribeInfo = navigator::navigateToSubscribeInfo,
//            navigateToSubscribeDetail = navigator::navigateToSubscribeDetail,
//            navigateToNotice = navigator::navigateToNotice,
//            navigateToNoticeDetail = navigator::navigateToNoticeDetail,
//            navigateToServiceCenter = navigator::navigateToServiceCenter,
//            navigateToUserInfo = navigator::navigateToUserInfo,
//            navigateToUserInfoSetting = navigator::navigateToUserInfoSetting,
//            navigateToLoginAfterLogout = navigator::navigateToLoginAfterLogout
//        )

        composable<Route.Alarm> {
            AlarmScreen(
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        // 로그인 내비게이션
        composable<Route.LoginStart> {
            LoginStartScreen(
                navigateToPhone = { navController.navigate(Route.LoginPhone) },
                navigateToRegisterElder = { navController.navigate(Route.LoginRegisterElder) },
                navigateToCareCallSetting = { navController.navigate(Route.LoginCareCallSetting) },
                navigateToPurchase = { navController.navigate(MainTabRoute.Home) },
                navigateToHome = {
                    navController.navigate(MainTabRoute.Home) {
                        popUpTo(Route.LoginStart) {
                            inclusive = true
                        }
                    }
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
                navigateToPurchase = { navController.navigate(MainTabRoute.Home) },
                navigateToHome = {
                    navController.navigate(MainTabRoute.Home) {
                        popUpTo(Route.LoginStart) {
                            inclusive = true
                        }
                    }
                },
                loginViewModel = loginViewModel,
            )
        }
        composable<Route.LoginRegisterUserInfo> {
            LoginMyInfoScreen(
                onBack = { navController.popBackStack() },
                navigateToRegisterElder = {
                    navController.navigate(Route.LoginRegisterElder)
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
                    navController.navigate(Route.LoginPurchase)
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
