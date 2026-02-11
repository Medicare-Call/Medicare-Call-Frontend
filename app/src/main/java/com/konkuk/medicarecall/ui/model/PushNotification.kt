package com.konkuk.medicarecall.ui.model

data class PushNotification(
    val all: String = "",
    val carecallCompleted: String = "",
    val healthAlert: String = "",
    val carecallMissed: String = "",
) {
    val isAllEnabled: Boolean get() = all == "ON"
    val isCarecallCompletedEnabled: Boolean get() = carecallCompleted == "ON"
    val isHealthAlertEnabled: Boolean get() = healthAlert == "ON"
    val isCarecallMissedEnabled: Boolean get() = carecallMissed == "ON"
}
