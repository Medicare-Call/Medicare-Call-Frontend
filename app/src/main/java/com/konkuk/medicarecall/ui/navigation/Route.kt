package com.konkuk.medicarecall.ui.navigation

sealed class Route(val route: String) {
    object AppSplash : Route("app_splash")
    object LoginStart : Route("login_start")
    object LoginPhone : Route("login_phone")
    object LoginVerification : Route("login_verification")
    object LoginMyInfo : Route("login_my_info")
    object LoginElderInfoScreen : Route("login_elder_info")
    object LoginElderMedInfoScreen : Route("login_elder_med_info")
    object SetCall : Route("set_call")
    object Payment : Route("payment")
    object NaverPay : Route("naver_pay")
    object NaverPayWithCode : Route("naver_pay/{orderCode}") {
        fun create(orderCode: String) = "naver_pay/$orderCode"
    }

    object FinishSplash : Route("finish_splash")
    object Home : Route("home")
    object Statistics : Route("statistics")
    object Settings : Route("settings")

    object Alarm : Route("alarm")

    object Announcement : Route("announcement")
    object AnnouncementDetail : Route("announcement_detail")
    object HealthInfo : Route("health_info")
    object HealthDetail : Route("health_detail")
    object MyDataSetting : Route("my_data_setting")
    object MyDetail : Route("my_detail")
    object PersonalDetail : Route("personal_detail")
    object PersonalInfo : Route("personal_info")
    object ServiceCenter : Route("service_center")
    object SettingAlarm : Route("setting_alarm")
    object SettingSubscribe : Route("setting_subscribe")
    object SubscribeDetail : Route("subscribe_detail")
    object MealDetail : Route("home_meal_detail")
    object MedicineDetail : Route("home_medicine_detail")
    object SleepDetail : Route("home_sleep_detail")
    object StateHealthDetail : Route("home_state_health_detail")
    object StateMentalDetail : Route("home_state_mental_detail")
    object GlucoseDetail : Route("home_glucose_detail")
}
