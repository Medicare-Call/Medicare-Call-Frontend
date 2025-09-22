package com.konkuk.medicarecall.navigation

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)
