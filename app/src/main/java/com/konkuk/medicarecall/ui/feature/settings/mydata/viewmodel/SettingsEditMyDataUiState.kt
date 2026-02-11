package com.konkuk.medicarecall.ui.feature.settings.mydata.viewmodel

import com.konkuk.medicarecall.domain.model.UserInfo

data class SettingsEditMyDataUiState(
    val masterChecked: Boolean = false,
    val completeChecked: Boolean = false,
    val abnormalChecked: Boolean = false,
    val missedChecked: Boolean = false,
    val isMale: Boolean = false,
    val name: String = "",
    val birth: String = "",
    val myDataInfo: UserInfo? = null,
    val isLoading: Boolean = false,
    val isUpdateSuccess: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasError: Boolean get() = errorMessage != null
    val hasData: Boolean get() = myDataInfo != null
    val allNotificationsEnabled: Boolean get() = masterChecked && completeChecked && abnormalChecked && missedChecked
}
