package com.konkuk.medicarecall.ui.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.konkuk.medicarecall.ui.feature.settings.screen.AnnouncementDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.AnnouncementScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.ElderDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.ElderInfoScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.HealthDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.HealthInfoScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.MyDataSettingScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.MyDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.ServiceCenterScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingAlarmScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingSubscribeScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SettingsScreen
import com.konkuk.medicarecall.ui.feature.settings.screen.SubscribeDetailScreen
import com.konkuk.medicarecall.ui.navigation.MainTabRoute
import com.konkuk.medicarecall.ui.navigation.Route

fun NavController.navigateToSettings(navOptions: NavOptions) {
    navigate(MainTabRoute.Settings, navOptions)
}

fun NavController.navigateToElderPersonalInfo() {
    navigate(Route.ElderPersonalInfo)
}

fun NavController.navigateToElderPersonalDetail(elderId: Int = -1) {
    navigate(Route.ElderPersonalDetail(elderId))
}

fun NavController.navigateToElderHealthInfo() {
    navigate(Route.ElderHealthInfo)
}

fun NavController.navigateToElderHealthDetail(elderId: Int) {
    navigate(Route.ElderHealthDetail(elderId))
}
fun NavController.navigateToNotificationSetting() {
    navigate(Route.NotificationSetting)
}

fun NavController.navigateToSubscribeInfo() {
    navigate(Route.SubscribeInfo)
}

fun NavController.navigateToSubscribeDetail(elderId: Int) {
    navigate(Route.SubscribeDetail(elderId))
}

fun NavController.navigateToNotice() {
    navigate(Route.Notice)
}

fun NavController.navigateToNoticeDetail(noticeId: Int) {
    navigate(Route.NoticeDetail(noticeId))
}

fun NavController.navigateToServiceCenter() {
    navigate(Route.ServiceCenter)
}

fun NavController.navigateToUserInfo() {
    navigate(Route.UserInfo)
}

fun NavController.navigateToUserInfoSetting() {
    navigate(Route.UserInfoSetting)
}

fun NavGraphBuilder.settingNavGraph(
    popBackStack: () -> Unit,
    navigateToElderPersonalInfo: () -> Unit,
    navigateToElderPersonalDetail: (Int) -> Unit,
    navigateToElderHealthInfo: () -> Unit,
    navigateToHealthDetail: (Int) -> Unit,
    navigateToNotificationSetting: () -> Unit,
    navigateToSubscribeInfo: () -> Unit,
    navigateToSubscribeDetail: (Int) -> Unit,
    navigateToNotice: () -> Unit,
    navigateToNoticeDetail: (Int) -> Unit,
    navigateToServiceCenter: () -> Unit,
    navigateToUserInfo: () -> Unit,
    navigateToUserInfoSetting: () -> Unit,
    navigateToLoginAfterLogout: () -> Unit,
    navController: NavHostController,
) {
    composable<MainTabRoute.Settings> {
        SettingsScreen(
            navigateToUserInfo = navigateToUserInfo,
            navigateToNotice = navigateToNotice,
            navigateToCenter = navigateToServiceCenter,
            navigateToSubscribe = navigateToSubscribeInfo,
            navigateToElderPersonalInfo = navigateToElderPersonalInfo,
            navigateToElderHealthInfo = navigateToElderHealthInfo,
            navigateToNotificationSetting = navigateToNotificationSetting,
        )
    }

    composable<Route.ElderPersonalInfo> {
        ElderInfoScreen(
            onBack = popBackStack,
            navigateToElderDetail = navigateToElderPersonalDetail,
        )
    }

    composable<Route.ElderPersonalDetail> { navBackstackEntry ->
        val elderId = navBackstackEntry.toRoute<Route.ElderPersonalDetail>().elderId
        ElderDetailScreen(
            elderId = elderId, // Screen에 ID만 전달
            onBack = popBackStack,
            navController = navController,
        )
    }

    composable<Route.ElderHealthInfo> {
        HealthInfoScreen(
            onBack = popBackStack,
            navigateToHealthDetail = navigateToHealthDetail,
        )
    }
    composable<Route.ElderHealthDetail> { navBackstackEntry ->
        val elderId = navBackstackEntry.toRoute<Route.ElderHealthDetail>().elderId
        HealthDetailScreen(
            elderId = elderId,
            onBack = popBackStack,
        )
    }

    composable<Route.NotificationSetting> {
        SettingAlarmScreen(
            onBack = popBackStack,
        )
    }

    composable<Route.SubscribeInfo> {
        SettingSubscribeScreen(
            onBack = popBackStack,
            navigateToSubscribeDetail = navigateToSubscribeDetail,
        )
    }
    composable<Route.SubscribeDetail> { navBackStackEntry -> val elderId = navBackStackEntry.toRoute<Route.SubscribeDetail>().elderId
        SubscribeDetailScreen(
            elderId = elderId,
            onBack = popBackStack,
        )
    }

    composable<Route.Notice> {
        AnnouncementScreen(
            onBack = popBackStack,
            navigateToNoticeDetail = navigateToNoticeDetail,
        )
    }


    composable<Route.NoticeDetail> { navBackStackEntry ->
        val noticeId = navBackStackEntry.toRoute<Route.NoticeDetail>().noticeId
        AnnouncementDetailScreen(
            noticeId = noticeId, // Screen에 ID만 전달
            onBack = popBackStack,
        )
    }

    composable<Route.ServiceCenter> {
        ServiceCenterScreen(
            onBack = popBackStack,
        )
    }

    composable<Route.UserInfo> {
        MyDataSettingScreen(
            onBack = popBackStack,
            navigateToUserInfoSetting = navigateToUserInfoSetting,
            navigateToLoginAfterLogout = navigateToLoginAfterLogout,
        )
    }


    composable<Route.UserInfoSetting> { MyDetailScreen(onBack = popBackStack)
    }
}
