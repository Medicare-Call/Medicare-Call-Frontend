package com.konkuk.medicarecall.ui.navigation

import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import kotlinx.serialization.Serializable

sealed interface Route {

    // 로그인 및 온보딩
    @Serializable
    data object Splash : Route

    @Serializable
    data object LoginStart : Route

    @Serializable
    data object LoginPhone : Route

    @Serializable
    data object LoginVerification : Route

    @Serializable
    data object LoginRegisterUserInfo : Route

    @Serializable
    data object LoginRegisterElder : Route

    @Serializable
    data object LoginRegisterElderHealth : Route

    @Serializable
    data object LoginCareCallSetting : Route

    @Serializable
    data object LoginPurchase : Route

    @Serializable
    data object LoginNaverPayView : Route

    @Serializable
    data object LoginFinish : Route

    // 홈 (하루 요약)
    @Serializable
    data object MealDetail : Route

    @Serializable
    data object MedicationDetail : Route

    @Serializable
    data object SleepDetail : Route

    @Serializable
    data object HealthAnalysisDetail : Route

    @Serializable
    data object MentalAnalysisDetail : Route

    @Serializable
    data object GlucoseDetail : Route

    // 설정
    @Serializable
    data object ElderPersonalInfo : Route

    @Serializable
    data class ElderPersonalDetail(val info: EldersInfoResponseDto) : Route

    @Serializable
    data object ElderHealthInfo : Route

    @Serializable
    data class ElderHealthDetail(val health: EldersHealthResponseDto) : Route

    @Serializable
    data class NotificationSetting(val myInfo: MyInfoResponseDto) : Route

    @Serializable
    data object SubscribeInfo : Route

    @Serializable
    data class SubscribeDetail(val subscription: EldersSubscriptionResponseDto) : Route

    @Serializable
    data object Notice : Route

    @Serializable
    data class NoticeDetail(val notice: NoticesResponseDto) : Route

    @Serializable
    data object ServiceCenter : Route

    @Serializable
    data object UserInfo : Route

    @Serializable
    data class UserInfoSetting(val myInfo: MyInfoResponseDto) : Route

    // 알림
    @Serializable
    data object Alarm : Route
}

sealed interface MainTabRoute : Route {
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object WeeklyStatistics : MainTabRoute

    @Serializable
    data object Settings : MainTabRoute
}
