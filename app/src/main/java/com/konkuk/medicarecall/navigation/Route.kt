package com.konkuk.medicarecall.navigation

sealed class Route(val route: String) {
    object LoginStart : Route("login_start")
    object Home : Route("home")
    object Statistics : Route("statistics")
    object Settings : Route("settings")
}