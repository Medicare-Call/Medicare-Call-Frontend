package com.konkuk.medicarecall.ui.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.konkuk.medicarecall.ui.feature.settings.elderhealth.screen.SettingsElderHealthDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.elderhealth.screen.SettingsElderHealthScreen
import com.konkuk.medicarecall.ui.feature.settings.elderinfo.screen.SettingsElderInfoDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.elderinfo.screen.SettingsElderInfoScreen
import com.konkuk.medicarecall.ui.feature.settings.menu.screen.SettingsMenuScreen
import com.konkuk.medicarecall.ui.feature.settings.mydata.screen.SettingsEditMyDataScreen
import com.konkuk.medicarecall.ui.feature.settings.mydata.screen.SettingsMyDataScreen
import com.konkuk.medicarecall.ui.feature.settings.notice.screen.SettingsNoticeDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.notice.screen.SettingsNoticeScreen
import com.konkuk.medicarecall.ui.feature.settings.notification.screen.SettingsNotificationScreen
import com.konkuk.medicarecall.ui.feature.settings.subscription.screen.SettingsSubscriptionDetailScreen
import com.konkuk.medicarecall.ui.feature.settings.subscription.screen.SettingsSubscriptionScreen
import com.konkuk.medicarecall.ui.feature.settings.support.screen.SettingsSupportScreen
import com.konkuk.medicarecall.ui.navigation.MainTabRoute
import com.konkuk.medicarecall.ui.navigation.Route

fun NavController.navigateToSettings(navOptions: NavOptions) {
    navigate(MainTabRoute.Settings, navOptions)
}

fun NavController.navigateToElderPersonalInfo() {
    navigate(Route.ElderPersonalInfo)
}

fun NavController.navigateToElderPersonalDetail(elderId: Long = -1L) {
    navigate(Route.ElderPersonalDetail(elderId))
}

fun NavController.navigateToElderHealthInfo() {
    navigate(Route.ElderHealthInfo)
}

fun NavController.navigateToElderHealthDetail(elderId: Long) {
    navigate(Route.ElderHealthDetail(elderId))
}

fun NavController.navigateToNotificationSetting() {
    navigate(Route.NotificationSetting)
}

fun NavController.navigateToSubscribeInfo() {
    navigate(Route.SubscribeInfo)
}

fun NavController.navigateToSubscribeDetail(elderId: Long) {
    navigate(Route.SubscribeDetail(elderId))
}

fun NavController.navigateToNotice() {
    navigate(Route.Notice)
}

fun NavController.navigateToNoticeDetail(noticeId: Long) {
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
    navigateToElderPersonalDetail: (Long) -> Unit,
    navigateToElderHealthInfo: () -> Unit,
    navigateToHealthDetail: (Long) -> Unit,
    navigateToNotificationSetting: () -> Unit,
    navigateToSubscribeInfo: () -> Unit,
    navigateToSubscribeDetail: (Long) -> Unit,
    navigateToNotice: () -> Unit,
    navigateToNoticeDetail: (Long) -> Unit,
    navigateToServiceCenter: () -> Unit,
    navigateToUserInfo: () -> Unit,
    navigateToUserInfoSetting: () -> Unit,
    navigateToLoginAfterLogout: () -> Unit,
    navController: NavHostController,
) {
    composable<MainTabRoute.Settings> {
        SettingsMenuScreen(
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
        SettingsElderInfoScreen(
            onBack = popBackStack,
            navigateToElderDetail = navigateToElderPersonalDetail,
        )
    }

    composable<Route.ElderPersonalDetail> { navBackstackEntry ->
        val elderId = navBackstackEntry.toRoute<Route.ElderPersonalDetail>().elderId
        SettingsElderInfoDetailScreen(
            elderId = elderId, // Screen에 ID만 전달
            onBack = popBackStack,
            navController = navController,
        )
    }

    composable<Route.ElderHealthInfo> {
        SettingsElderHealthScreen(
            onBack = popBackStack,
            navigateToHealthDetail = navigateToHealthDetail,
        )
    }
    composable<Route.ElderHealthDetail> { navBackstackEntry ->
        val elderId = navBackstackEntry.toRoute<Route.ElderHealthDetail>().elderId
        SettingsElderHealthDetailScreen(
            elderId = elderId,
            onBack = popBackStack,
        )
    }

    composable<Route.NotificationSetting> {
        SettingsNotificationScreen(
            onBack = popBackStack,
        )
    }

    composable<Route.SubscribeInfo> {
        SettingsSubscriptionScreen(
            onBack = popBackStack,
            navigateToSubscribeDetail = navigateToSubscribeDetail,
        )
    }
    composable<Route.SubscribeDetail> { navBackStackEntry ->
        val elderId = navBackStackEntry.toRoute<Route.SubscribeDetail>().elderId
        SettingsSubscriptionDetailScreen(
            elderId = elderId,
            onBack = popBackStack,
        )
    }

    composable<Route.Notice> {
        SettingsNoticeScreen(
            onBack = popBackStack,
            navigateToNoticeDetail = navigateToNoticeDetail,
        )
    }

    composable<Route.NoticeDetail> { navBackStackEntry ->
        val noticeId = navBackStackEntry.toRoute<Route.NoticeDetail>().noticeId
        SettingsNoticeDetailScreen(
            noticeId = noticeId, // Screen에 ID만 전달
            onBack = popBackStack,
        )
    }

    composable<Route.ServiceCenter> {
        SettingsSupportScreen(
            onBack = popBackStack,
        )
    }

    composable<Route.UserInfo> {
        SettingsMyDataScreen(
            onBack = popBackStack,
            navigateToUserInfoSetting = navigateToUserInfoSetting,
            navigateToLoginAfterLogout = navigateToLoginAfterLogout,
        )
    }
    composable<Route.UserInfoSetting> {
        SettingsEditMyDataScreen(onBack = popBackStack)
    }
}
