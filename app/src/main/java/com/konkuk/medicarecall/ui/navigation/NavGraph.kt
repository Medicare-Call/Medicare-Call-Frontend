package com.konkuk.medicarecall.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.ui.feature.alarm.screen.AlarmScreen
import com.konkuk.medicarecall.ui.feature.home.screen.HomeScreen
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeViewModel
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.screen.GlucoseDetail
import com.konkuk.medicarecall.ui.feature.homedetail.meal.screen.MealDetail
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.screen.MedicineDetail
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.screen.SleepDetail
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.screen.StateHealthDetail
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.screen.StateMentalDetail
import com.konkuk.medicarecall.ui.feature.login.carecall.screen.SetCallScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginMyInfoScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginPhoneScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginStartScreen
import com.konkuk.medicarecall.ui.feature.login.info.screen.LoginVerificationScreen
import com.konkuk.medicarecall.ui.feature.login.info.viewmodel.LoginViewModel
import com.konkuk.medicarecall.ui.feature.login.payment.screen.FinishSplashScreen
import com.konkuk.medicarecall.ui.feature.login.payment.screen.NaverPayScreen
import com.konkuk.medicarecall.ui.feature.login.payment.screen.PaymentScreen
import com.konkuk.medicarecall.ui.feature.login.senior.LoginElderViewModel
import com.konkuk.medicarecall.ui.feature.login.senior.screen.LoginElderMedInfoScreen
import com.konkuk.medicarecall.ui.feature.login.senior.screen.LoginElderScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.AnnouncementDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.AnnouncementScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.HealthDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.HealthInfoScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.MyDataSettingScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.MyDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.PersonalDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.PersonalInfoScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.ServiceCenterScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingAlarmScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingSubscribeScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingsScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SubscribeDetailScreen
import com.konkuk.medicarecall.ui.feature.splash.screen.SplashScreen
import com.konkuk.medicarecall.ui.feature.statistics.screen.StatisticsScreen
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// ---- 헬퍼: 로그인 성공 후 인증 그래프 제거하고 main으로 ---
fun NavHostController.navigateToMainAfterLogin() {
    navigate(MainTabRoute.DailySummary) {
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
        startDestination = Route.Splash,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier,
    ) {
        composable<Route.Splash> {
            SplashScreen(navController)
        }

        // 홈
        composable<MainTabRoute.DailySummary> { backStackEntry ->
            HomeScreen(
                navController = navController,
                onNavigateToMealDetail = { navController.navigate(Route.MealDetail) },
                onNavigateToMedicineDetail = { navController.navigate(Route.MedicationDetail) },
                onNavigateToSleepDetail = { navController.navigate(Route.SleepDetail) },
                onNavigateToStateHealthDetail = { navController.navigate(Route.HealthAnalysisDetail) },
                onNavigateToStateMentalDetail = { navController.navigate(Route.MentalAnalysisDetail) },
                onNavigateToGlucoseDetail = { navController.navigate(Route.GlucoseDetail) },
            )
        }

        // 홈 상세 화면_식사 화면
        composable<Route.MealDetail> {
            MealDetail(
                navController = navController,
            )
        }


        // 홈 상세 화면_복용 화면
        composable<Route.MedicationDetail> {
            MedicineDetail(
                navController = navController,
            )
        }


        //홈 상세 화면_수면 화면
        composable<Route.SleepDetail> {
            SleepDetail(
                navController = navController,
            )
        }


        //홈 상세 화면_건강 징후 화면
        composable<Route.HealthAnalysisDetail> {
            StateHealthDetail(
                navController = navController,
            )
        }

        //홈 상세 화면_심리 상태 화면
        composable<Route.MentalAnalysisDetail> {
            StateMentalDetail(
                navController = navController,
            )
        }


        //홈 상세 화면_혈당 화면

        composable<Route.GlucoseDetail> {
            GlucoseDetail(navController = navController)
        }


        // 통계
        composable<MainTabRoute.WeeklyStatistics> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainTabRoute.DailySummary)
            }
            val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

            StatisticsScreen(
                navController = navController,
                homeViewModel = homeViewModel,
            )
        }

        // 설정
        composable<MainTabRoute.Settings> {
            //TopLevelBackHandler(navController)
            SettingsScreen(
                onNavigateToMyDataSetting = {
                    navController.navigate(Route.UserInfo)
                },
                onNavigateToAnnouncement = {
                    navController.navigate(Route.Notice)
                },
                onNavigateToCenter = {
                    navController.navigate(Route.ServiceCenter)
                },
                onNavigateToSubscribe = {
                    navController.navigate(Route.SubscribeInfo)
                },
                onNavigateToPersonalInfo = {
                    navController.navigate(Route.ElderPersonalInfo)
                },
                onNavigateToHealthInfo = {
                    navController.navigate(Route.ElderHealthInfo)
                },
                navController = navController,
            )
        }

        composable<Route.UserInfo> {
            MyDataSettingScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        composable(
            route = "my_detail/{myDataJson}",
            arguments = listOf(navArgument("myDataJson") { type = NavType.StringType }),
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("myDataJson") ?: ""
            val decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val myDataInfo = Json.decodeFromString<MyInfoResponseDto>(decodedJson)
            MyDetailScreen(
                myDataInfo = myDataInfo,
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        composable<Route.Notice> {
            AnnouncementScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        composable(
            route = "announcement_detail/{noticeJson}",
            arguments = listOf(navArgument("noticeJson") { type = NavType.StringType }),
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("noticeJson") ?: ""
            val decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val noticeInfo = Json.decodeFromString<NoticesResponseDto>(decodedJson)

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

        composable<Route.SubscribeInfo> {
            SettingSubscribeScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        composable(
            route = "subscribe_detail/{elderJson}",
            // elderJson을 NavArgument로 받아옴
            arguments = listOf(navArgument("elderJson") { type = NavType.StringType }),
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("elderJson") ?: ""
            val decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val elderInfo = Json.decodeFromString<EldersSubscriptionResponseDto>(decodedJson)

            SubscribeDetailScreen(
                elderInfo = elderInfo,
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.ElderPersonalInfo> {
            PersonalInfoScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        composable(
            route = "personal_detail/{elderInfo}",
            arguments = listOf(
                navArgument("elderInfo") {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val encodedElderInfo = backStackEntry.arguments?.getString("elderInfo") ?: ""
            val decodedElderInfo =
                URLDecoder.decode(encodedElderInfo, StandardCharsets.UTF_8.toString())
            val eldersInfoResponseDto =
                Json.decodeFromString<EldersInfoResponseDto>(decodedElderInfo)
            PersonalDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                eldersInfoResponseDto = eldersInfoResponseDto,
            )
        }

        composable<Route.ElderHealthInfo> {
            HealthInfoScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        composable(
            route = "health_detail/{healthInfo}",
            arguments = listOf(
                navArgument("healthInfo") {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val encodedHealthInfo = backStackEntry.arguments?.getString("healthInfo") ?: ""
            val decodedHealthInfo =
                URLDecoder.decode(encodedHealthInfo, StandardCharsets.UTF_8.toString())
            val healthInfoResponseDto =
                Json.decodeFromString<EldersHealthResponseDto>(decodedHealthInfo)
            HealthDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                healthInfoResponseDto = healthInfoResponseDto,

                )
        }

        composable(
            route = "setting_alarm/{myDataJson}",
            arguments = listOf(navArgument("myDataJson") { type = NavType.StringType }),
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("myDataJson") ?: ""
            val decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val myDataInfo = Json.decodeFromString<MyInfoResponseDto>(decodedJson)

            SettingAlarmScreen(
                myDataInfo = myDataInfo,
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        composable<Route.Alarm> {
            AlarmScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        // 로그인 내비게이션
        composable<Route.LoginStart> {
            LoginStartScreen(navController, loginViewModel)
        }
        composable<Route.LoginPhone> {
            LoginPhoneScreen(navController, loginViewModel)
        }
        composable<Route.LoginVerification> {
            LoginVerificationScreen(navController, loginViewModel)
        }
        composable<Route.LoginRegisterUserInfo> {
            LoginMyInfoScreen(navController, loginViewModel)
        }
        composable<Route.LoginRegisterElder> {
            LoginElderScreen(navController, loginElderViewModel)
        }
        composable<Route.LoginRegisterElderHealth> {
            LoginElderMedInfoScreen(navController, loginElderViewModel)
        }

        composable<Route.LoginCareCallSetting> {
            SetCallScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        composable<Route.LoginPurchase> {
            PaymentScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        composable<Route.LoginNaverPayView> {
            NaverPayScreen(
                onBack = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

        composable<Route.LoginFinish> {
            FinishSplashScreen(
                navController = navController,
            )
        }
    }

}
