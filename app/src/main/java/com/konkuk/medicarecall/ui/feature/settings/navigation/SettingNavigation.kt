package com.konkuk.medicarecall.ui.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
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
import com.konkuk.medicarecall.ui.navigation.EldersHealthResponseDtoType
import com.konkuk.medicarecall.ui.navigation.EldersInfoResponseDtoType
import com.konkuk.medicarecall.ui.navigation.EldersSubscriptionResponseDtoType
import com.konkuk.medicarecall.ui.navigation.MainTabRoute
import com.konkuk.medicarecall.ui.navigation.MyInfoResponseDtoType
import com.konkuk.medicarecall.ui.navigation.NoticesResponseDtoType
import com.konkuk.medicarecall.ui.navigation.Route
import kotlin.reflect.typeOf

fun NavController.navigateToSettings(navOptions: NavOptions) {
    navigate(MainTabRoute.Settings, navOptions)
}

fun NavController.navigateToElderPersonalInfo() {
    navigate(Route.ElderPersonalInfo)
}

fun NavController.navigateToElderPersonalDetail(info: EldersInfoResponseDto) {
    navigate(Route.ElderPersonalDetail(info))
}

fun NavController.navigateToElderHealthInfo() {
    navigate(Route.ElderHealthInfo)
}

fun NavController.navigateToElderHealthDetail(health: EldersHealthResponseDto) { // EldersHealthResponseDto
    navigate(Route.ElderHealthDetail(health))
}

fun NavController.navigateToNotificationSetting(myInfo: MyInfoResponseDto) {
    navigate(Route.NotificationSetting(myInfo))
}

fun NavController.navigateToSubscribeInfo() {
    navigate(Route.SubscribeInfo)
}

fun NavController.navigateToSubscribeDetail(subscription: EldersSubscriptionResponseDto) {
    navigate(Route.SubscribeDetail(subscription))
}

fun NavController.navigateToNotice() {
    navigate(Route.Notice)
}

fun NavController.navigateToNoticeDetail(notice: NoticesResponseDto) {
    navigate(Route.NoticeDetail(notice))
}

fun NavController.navigateToServiceCenter() {
    navigate(Route.ServiceCenter)
}

fun NavController.navigateToUserInfo() {
    navigate(Route.UserInfo)
}

fun NavController.navigateToUserInfoSetting(myInfo: MyInfoResponseDto) {
    navigate(Route.UserInfoSetting(myInfo))
}

fun NavGraphBuilder.settingNavGraph(
    popBackStack: () -> Unit,
    navigateToElderPersonalInfo: () -> Unit,
    navigateToElderPersonalDetail: (EldersInfoResponseDto) -> Unit,
    navigateToElderHealthInfo: () -> Unit,
    navigateToHealthDetail: (EldersHealthResponseDto) -> Unit,
    navigateToNotificationSetting: (MyInfoResponseDto) -> Unit,
    navigateToSubscribeInfo: () -> Unit,
    navigateToSubscribeDetail: (EldersSubscriptionResponseDto) -> Unit,
    navigateToNotice: () -> Unit,
    navigateToNoticeDetail: (NoticesResponseDto) -> Unit,
    navigateToServiceCenter: () -> Unit,
    navigateToUserInfo: () -> Unit,
    navigateToUserInfoSetting: (MyInfoResponseDto) -> Unit,
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

    composable<Route.ElderPersonalDetail>(
        typeMap = mapOf(typeOf<EldersInfoResponseDto>() to EldersInfoResponseDtoType),
    ) { navBackstackEntry ->
        ElderDetailScreen(
            onBack = popBackStack,
            eldersInfoResponseDto = navBackstackEntry.toRoute<Route.ElderPersonalDetail>().info,
            navController = navController,
        )
    }

    composable<Route.ElderHealthInfo> {
        HealthInfoScreen(
            onBack = popBackStack,
            navigateToHealthDetail = navigateToHealthDetail,
        )
    }

    composable<Route.ElderHealthDetail>(
        typeMap = mapOf(typeOf<EldersHealthResponseDto>() to EldersHealthResponseDtoType),
    ) { navBackstackEntry ->
        HealthDetailScreen(
            onBack = popBackStack,
            healthInfoResponseDto = navBackstackEntry.toRoute<Route.ElderHealthDetail>().health,
        )
    }

    composable<Route.NotificationSetting>(
        typeMap = mapOf(typeOf<MyInfoResponseDto>() to MyInfoResponseDtoType),
    ) { navBackStackEntry ->
        SettingAlarmScreen(
            myDataInfo = navBackStackEntry.toRoute<Route.NotificationSetting>().myInfo,
            onBack = popBackStack,
        )
    }

    composable<Route.SubscribeInfo> {
        SettingSubscribeScreen(
            onBack = popBackStack,
            navigateToSubscribeDetail = navigateToSubscribeDetail,
        )
    }

    composable<Route.SubscribeDetail>(
        typeMap = mapOf(typeOf<EldersSubscriptionResponseDto>() to EldersSubscriptionResponseDtoType),
    ) { navBackStackEntry ->
        SubscribeDetailScreen(
            elderInfo = navBackStackEntry.toRoute<Route.SubscribeDetail>().subscription,
            onBack = popBackStack,
        )
    }

    composable<Route.Notice> {
        AnnouncementScreen(
            onBack = popBackStack,
            navigateToNoticeDetail = navigateToNoticeDetail,
        )
    }

    composable<Route.NoticeDetail>(
        typeMap = mapOf(typeOf<NoticesResponseDto>() to NoticesResponseDtoType),
    ) { navBackStackEntry ->
        AnnouncementDetailScreen(
            noticeInfo = navBackStackEntry.toRoute<Route.NoticeDetail>().notice,
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

    composable<Route.UserInfoSetting>(
        typeMap = mapOf(typeOf<MyInfoResponseDto>() to MyInfoResponseDtoType),
    ) { navBackStackEntry ->
        MyDetailScreen(
            myDataInfo = navBackStackEntry.toRoute<Route.UserInfoSetting>().myInfo,
            onBack = popBackStack,
        )
    }
}
