package com.konkuk.medicarecall.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.ui.feature.alarm.screen.AlarmScreen
import com.konkuk.medicarecall.ui.feature.calendar.viewmodel.CalendarViewModel
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


// ----- 헬퍼: 탑레벨 전환은 back stack 확장 없이 -----
fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        // 그래프 시작점까지 popUp + 상태 저장/복원
        popUpTo(Route.Home.route) {
            inclusive = false
            saveState = true
        }
//        launchSingleTop = true
        restoreState = true
    }
}

// ---- 헬퍼: 로그인 성공 후 인증 그래프 제거하고 main으로 ---
fun NavHostController.navigateToMainAfterLogin() {
    navigate("main") {
        popUpTo("login") { inclusive = true }
        launchSingleTop = true
        restoreState = true
    }
}

// ----- 컴포저블: 탑레벨에서 뒤로가기 = 앱 백그라운드 이동 -----
@Composable
private fun TopLevelBackHandler(navController: NavHostController) {
    val activity = LocalContext.current as? Activity
    val topLevel = setOf(Route.Home.route, Route.Statistics.route, Route.Settings.route)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isTopLevel = currentRoute in topLevel

    if (isTopLevel && activity != null) {
        BackHandler(true) {
            activity.moveTaskToBack(true)
        }
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    loginElderViewModel: LoginElderViewModel,
    modifier: Modifier = Modifier
) {
//    val startDestination = if (loginViewModel.isLoggedIn) "main" else "login"
    // navController = navController, startDestination = Route.Home.route, // 시작 화면
    val calendarViewModel: CalendarViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Route.AppSplash.route, // 시작 화면
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier
    ) {
        composable(route = Route.AppSplash.route) {
            SplashScreen(navController)
        }


        // 메인 내비게이션
        navigation(startDestination = Route.Home.route, route = "main") {


            // 홈
            composable(route = Route.Home.route) { backStackEntry ->
                //TopLevelBackHandler(navController)
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("main")
                }
                val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

                HomeScreen(
                    navController = navController,
                    onNavigateToMealDetail = { navController.navigate(Route.MealDetail.route) },
                    onNavigateToMedicineDetail = { navController.navigate(Route.MedicineDetail.route) },
                    onNavigateToSleepDetail = { navController.navigate(Route.SleepDetail.route) },
                    onNavigateToStateHealthDetail = { navController.navigate(Route.StateHealthDetail.route) },
                    onNavigateToStateMentalDetail = { navController.navigate(Route.StateMentalDetail.route) },
                    onNavigateToGlucoseDetail = { navController.navigate(Route.GlucoseDetail.route) },
                    homeViewModel = homeViewModel
                )
            }

            // 홈 상세 화면_식사 화면
            composable(route = Route.MealDetail.route) {
                MealDetail(
                    navController = navController
                )
            }


            // 홈 상세 화면_복용 화면
            composable(route = Route.MedicineDetail.route) {
                MedicineDetail(
                    navController = navController
                )
            }


            //홈 상세 화면_수면 화면
            composable(route = Route.SleepDetail.route) {
                SleepDetail(
                    navController = navController
                )
            }


            //홈 상세 화면_건강 징후 화면
            composable(route = Route.StateHealthDetail.route) {
                StateHealthDetail(
                    navController = navController
                )
            }

            //홈 상세 화면_심리 상태 화면
            composable(route = Route.StateMentalDetail.route) {
                StateMentalDetail(
                    navController = navController
                )
            }


            //홈 상세 화면_혈당 화면

            composable(route = Route.GlucoseDetail.route) {
                GlucoseDetail(navController = navController)
            }


            // 통계
            composable(route = Route.Statistics.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("main")
                }
                val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

                StatisticsScreen(
                    navController = navController,
                    homeViewModel = homeViewModel
                )
            }

            // 설정
            composable(route = Route.Settings.route) {
                //TopLevelBackHandler(navController)
                SettingsScreen(
                    onNavigateToMyDataSetting = {
                        navController.navigate(Route.MyDataSetting.route)
                    },
                    onNavigateToAnnouncement = {
                        navController.navigate(Route.Announcement.route)
                    },
                    onNavigateToCenter = {
                        navController.navigate(Route.ServiceCenter.route)
                    },
                    onNavigateToSubscribe = {
                        navController.navigate(Route.SettingSubscribe.route)
                    },
                    onNavigateToPersonalInfo = {
                        navController.navigate(Route.PersonalInfo.route)
                    },
                    onNavigateToHealthInfo = {
                        navController.navigate(Route.HealthInfo.route)
                    },
                    navController = navController
                )
            }

            composable(
                route = Route.MyDataSetting.route
            ) {
                MyDataSettingScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }

            composable(
                route = "my_detail/{myDataJson}",
                arguments = listOf(navArgument("myDataJson") { type = NavType.StringType })
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

            composable(route = Route.Announcement.route) {
                AnnouncementScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }

            composable(
                route = "announcement_detail/{noticeJson}",
                arguments = listOf(navArgument("noticeJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedJson = backStackEntry.arguments?.getString("noticeJson") ?: ""
                val decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
                val noticeInfo = Json.decodeFromString<NoticesResponseDto>(decodedJson)

                AnnouncementDetailScreen(
                    noticeInfo = noticeInfo,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = Route.ServiceCenter.route) {
                ServiceCenterScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Route.SettingSubscribe.route) {
                SettingSubscribeScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }

            composable(
                route = "subscribe_detail/{elderJson}",
                // elderJson을 NavArgument로 받아옴
                arguments = listOf(navArgument("elderJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedJson = backStackEntry.arguments?.getString("elderJson") ?: ""
                val decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
                val elderInfo = Json.decodeFromString<EldersSubscriptionResponseDto>(decodedJson)

                SubscribeDetailScreen(
                    elderInfo = elderInfo,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = Route.PersonalInfo.route) {
                PersonalInfoScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }

            composable(
                route = "personal_detail/{elderInfo}",
                arguments = listOf(navArgument("elderInfo") {
                    type = NavType.StringType
                })
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
                    eldersInfoResponseDto = eldersInfoResponseDto
                )
            }

            composable(route = Route.HealthInfo.route) {
                HealthInfoScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }

            composable(
                route = "health_detail/{healthInfo}",
                arguments = listOf(navArgument("healthInfo") {
                    type = NavType.StringType
                })
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
                arguments = listOf(navArgument("myDataJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedJson = backStackEntry.arguments?.getString("myDataJson") ?: ""
                val decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
                val myDataInfo = Json.decodeFromString<MyInfoResponseDto>(decodedJson)

                SettingAlarmScreen(
                    myDataInfo = myDataInfo,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Route.Alarm.route) {
                AlarmScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }
        }

        // 로그인 내비게이션
        navigation(startDestination = Route.LoginStart.route, route = "login") {


            composable(route = Route.LoginStart.route) {
                LoginStartScreen(navController, loginViewModel)
            }
            composable(route = Route.LoginPhone.route) {
                LoginPhoneScreen(navController, loginViewModel)
            }
            composable(route = Route.LoginVerification.route) {
                LoginVerificationScreen(navController, loginViewModel)
            }
            composable(route = Route.LoginMyInfo.route) {
                LoginMyInfoScreen(navController, loginViewModel)
            }
            composable(route = Route.LoginElderInfoScreen.route) {
                LoginElderScreen(navController, loginElderViewModel)
            }
            composable(route = Route.LoginElderMedInfoScreen.route) {
                LoginElderMedInfoScreen(navController, loginElderViewModel)
            }

            composable(route = Route.SetCall.route) {
                SetCallScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController,
                )
            }

            composable(route = Route.Payment.route) {
                PaymentScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController,
                )
            }

            composable(route = Route.NaverPay.route) {
                NaverPayScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }

            composable(route = Route.FinishSplash.route) {
                FinishSplashScreen(
                    navController = navController,
                )
            }

            composable(
                route = Route.NaverPayWithCode.route,
                arguments = listOf(navArgument("orderCode") { type = NavType.StringType })
            ) { backStackEntry ->
                NaverPayScreen(
                    onBack = { navController.popBackStack() },
                    navController = navController
                )
            }
        }


    }

}
