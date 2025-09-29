package com.konkuk.medicarecall.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object AppSplash : Route

    @Serializable
    data object LoginStart : Route

    @Serializable
    data object LoginPhone : Route

    @Serializable
    data object LoginVerification : Route

    @Serializable
    data object LoginMyInfo : Route

    @Serializable
    data object LoginElderInfoScreen : Route

    @Serializable
    data object LoginElderMedInfoScreen : Route

    @Serializable
    data object SetCall : Route

    @Serializable
    data object Payment : Route

    @Serializable
    data object NaverPay : Route

    @Serializable
    data class NaverPayWithCode(val orderCode: String) : Route

    @Serializable
    data object FinishSplash : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object Statistics : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data object Alarm : Route

    @Serializable
    data object Announcement : Route

    @Serializable
    data object AnnouncementDetail : Route

    @Serializable
    data object HealthInfo : Route

    @Serializable
    data object HealthDetail : Route

    @Serializable
    data object MyDataSetting : Route

    @Serializable
    data object MyDetail : Route

    @Serializable
    data object PersonalDetail : Route

    @Serializable
    data object PersonalInfo : Route

    @Serializable
    data object ServiceCenter : Route

    @Serializable
    data object SettingAlarm : Route

    @Serializable
    data object SettingSubscribe : Route

    @Serializable
    data object SubscribeDetail : Route

    @Serializable
    data object MealDetail : Route

    @Serializable
    data object MedicineDetail : Route

    @Serializable
    data object SleepDetail : Route

    @Serializable
    data object StateHealthDetail : Route

    @Serializable
    data object StateMentalDetail : Route

    @Serializable
    data object GlucoseDetail : Route
}

sealed interface MainTabRoute : Route {
    @Serializable
    data object DailySummary : MainTabRoute

    @Serializable
    data object WeeklyStatistics : MainTabRoute

    @Serializable
    data object Settings : MainTabRoute
}
