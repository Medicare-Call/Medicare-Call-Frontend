package com.konkuk.medicarecall.ui.navigation

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
    data object LoginFinish : Route

    // 홈 (하루 요약)
    @Serializable
    data class MealDetail(val elderId: Long) : Route

    @Serializable
    data class MedicineDetail(val elderId: Long) : Route

    @Serializable
    data class SleepDetail(val elderId: Long) : Route

    @Serializable
    data class StateHealthDetail(val elderId: Long) : Route

    @Serializable
    data class StateMentalDetail(val elderId: Long) : Route

    @Serializable
    data class GlucoseDetail(val elderId: Long) : Route

    // 설정
    @Serializable
    data object ElderPersonalInfo : Route

    @Serializable
    data class ElderPersonalDetail(val elderId: Long = -1L) : Route
    // 등록(-1)과 수정(id)을 통합하기 위해 기본값 추가

    @Serializable
    data object ElderHealthInfo : Route

    @Serializable
    data class ElderHealthDetail(val elderId: Long) : Route

    @Serializable
    data object NotificationSetting : Route

    @Serializable
    data object SubscribeInfo : Route

    @Serializable
    data class SubscribeDetail(val elderId: Long) : Route

    @Serializable
    data object Notice : Route

    @Serializable
    data class NoticeDetail(val noticeId: Long) : Route

    @Serializable
    data object ServiceCenter : Route

    @Serializable
    data object UserInfo : Route

    @Serializable
    data object UserInfoSetting : Route

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
