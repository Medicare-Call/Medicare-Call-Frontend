package com.konkuk.medicarecall.navigation

sealed class Route(val route: String) {
    object LoginStart : Route("login_start")
    object LoginPhone : Route("login_phone")
    object LoginVerification : Route("login_verification")
    object LoginMyInfo : Route("login_my_info")
    object Home : Route("home")
    object Statistics : Route("statistics")
    object Settings : Route("settings")
}