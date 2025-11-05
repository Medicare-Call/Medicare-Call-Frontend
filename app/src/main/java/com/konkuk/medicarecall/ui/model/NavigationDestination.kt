package com.konkuk.medicarecall.ui.model

sealed interface NavigationDestination {
    data object GoToLogin : NavigationDestination
    data object GoToRegisterElder : NavigationDestination
    data object GoToTimeSetting : NavigationDestination
    data object GoToPayment : NavigationDestination
    data object GoToHome : NavigationDestination
}
