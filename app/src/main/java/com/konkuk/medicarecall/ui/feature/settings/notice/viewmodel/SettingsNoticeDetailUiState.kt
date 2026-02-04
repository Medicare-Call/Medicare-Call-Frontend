package com.konkuk.medicarecall.ui.feature.settings.notice.viewmodel

import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto

data class SettingsNoticeDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val noticeData: NoticesResponseDto? = null,
)
