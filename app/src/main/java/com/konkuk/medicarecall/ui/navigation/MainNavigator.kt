package com.konkuk.medicarecall.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.ui.feature.alarm.navigation.navigateToAlarm
import com.konkuk.medicarecall.ui.feature.home.navigation.navigateToHome
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToGlucoseDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToHealthAnalysisDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToMealDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToMedicationDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToMentalAnalysisDetail
import com.konkuk.medicarecall.ui.feature.homedetail.navigation.navigateToSleepDetail
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginCareCallSetting
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginFinish
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginNaverPayView
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginPhone
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginPurchase
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginRegisterElder
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginRegisterElderHealth
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginRegisterUserInfo
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginStart
import com.konkuk.medicarecall.ui.feature.login.navigation.navigateToLoginVerification
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToElderHealthDetail
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToElderHealthInfo
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToElderPersonalDetail
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToElderPersonalInfo
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToNotice
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToNoticeDetail
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToNotificationSetting
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToServiceCenter
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToSettings
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToSubscribeDetail
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToSubscribeInfo
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToUserInfo
import com.konkuk.medicarecall.ui.feature.settings.navigation.navigateToUserInfoSetting
import com.konkuk.medicarecall.ui.feature.statistics.navigation.navigateToStatistics
import com.konkuk.medicarecall.ui.navigation.component.MainTab

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination
    val startDestination = Route.Splash

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    // 메인 탭 이동 함수
    fun navigateToMainTab(tab: MainTab) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions)
            MainTab.WEEKLY_STATISTICS -> navController.navigateToStatistics(navOptions)
            MainTab.SETTINGS -> navController.navigateToSettings(navOptions)
        }
    }

    // 뒤로 가기 함수
    fun popBackStack() {
        navController.popBackStack()
    }

    /* 로그인 */
    fun navigateToLoginStart() {
        navController.navigateToLoginStart()
    }

    fun navigateToLoginPhone() {
        navController.navigateToLoginPhone()
    }

    fun navigateToLoginVerification() {
        navController.navigateToLoginVerification()
    }

    fun navigateToLoginRegisterUserInfo() {
        navController.navigateToLoginRegisterUserInfo(
            navOptions {
                popUpTo(Route.LoginVerification) { inclusive = true }
            },
        )
    }

    fun navigateToLoginRegisterElder() {
        navController.navigateToLoginRegisterElder()
    }

    fun navigateToLoginRegisterElderHealth() {
        navController.navigateToLoginRegisterElderHealth()
    }

    fun navigateToLoginCareCallSetting() {
        navController.navigateToLoginCareCallSetting(
            navOptions {
                popUpTo(Route.LoginRegisterElder) {
                    inclusive = true
                }
            },
        )
    }

    fun navigateToLoginPurchase() {
        navController.navigateToLoginPurchase()
    }

    fun navigateToLoginNaverPayView() {
        navController.navigateToLoginNaverPayView()
    }

    fun navigateToLoginFinish() {
        navController.navigateToLoginFinish()
    }

    /* 홈 화면 */
    fun navigateToHome() {
        this.navigateToMainTab(MainTab.HOME)
    }

    fun navigateToMealDetail() {
        navController.navigateToMealDetail()
    }

    fun navigateToMedicationDetail() {
        navController.navigateToMedicationDetail()
    }

    fun navigateToSleepDetail() {
        navController.navigateToSleepDetail()
    }

    fun navigateToHealthAnalysisDetail() {
        navController.navigateToHealthAnalysisDetail()
    }

    fun navigateToMentalAnalysisDetail() {
        navController.navigateToMentalAnalysisDetail()
    }

    fun navigateToGlucoseDetail() {
        navController.navigateToGlucoseDetail()
    }

    /* 설정 화면 */
    fun navigateToElderPersonalInfo() {
        navController.navigateToElderPersonalInfo()
    }

    fun navigateToElderPersonalDetail(info: EldersInfoResponseDto) {
        navController.navigateToElderPersonalDetail(info)
    }

    fun navigateToHealthInfo() {
        navController.navigateToElderHealthInfo()
    }

    fun navigateToHealthDetail(health: EldersHealthResponseDto) { // EldersHealthResponseDto
        navController.navigateToElderHealthDetail(health)
    }

    fun navigateToNotificationSetting(myInfo: MyInfoResponseDto) {
        navController.navigateToNotificationSetting(myInfo)
    }

    fun navigateToSubscribeInfo() {
        navController.navigateToSubscribeInfo()
    }

    fun navigateToSubscribeDetail(subscription: EldersSubscriptionResponseDto) { // EldersSubscriptionResponseDto
        navController.navigateToSubscribeDetail(subscription)
    }

    fun navigateToNotice() {
        navController.navigateToNotice()
    }

    fun navigateToNoticeDetail(notice: NoticesResponseDto) {
        navController.navigateToNoticeDetail(notice)
    }

    fun navigateToServiceCenter() {
        navController.navigateToServiceCenter()
    }

    fun navigateToUserInfo() {
        navController.navigateToUserInfo()
    }

    fun navigateToUserInfoSetting(myInfo: MyInfoResponseDto) {
        navController.navigateToUserInfoSetting(myInfo)
    }

    fun navigateToLoginAfterLogout() {
        navController.navigate(Route.LoginStart) {
            popUpTo(MainTabRoute.Home) { inclusive = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    /* 알림 */
    fun navigateToAlarm() {
        navController.navigateToAlarm()
    }

    // 현재 화면이 BottomBar를 보여줘야 하는지 여부
    @Composable
    fun shouldShowBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
