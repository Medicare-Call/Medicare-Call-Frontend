package com.konkuk.medicarecall.ui.feature.settings.mydata.viewmodel

import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto

data class SettingsEditMyDataUiState(
    val masterChecked: Boolean = false,
    val completeChecked: Boolean = false,
    val abnormalChecked: Boolean = false,
    val missedChecked: Boolean = false,
    val isMale: Boolean = false,
    val name: String = "",
    val birth: String = "",
    val myDataInfo: MyInfoResponseDto? = null,
    val isLoading: Boolean = false,
    val isUpdateSuccess: Boolean = false,
    val errorMessage: String? = null,
)
