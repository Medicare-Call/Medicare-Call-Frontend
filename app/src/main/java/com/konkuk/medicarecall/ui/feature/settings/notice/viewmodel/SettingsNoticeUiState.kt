package com.konkuk.medicarecall.ui.feature.settings.notice.viewmodel

import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto

data class SettingsNoticeUiState(
    val noticeList: List<NoticesResponseDto> = emptyList(),
    val errorMessage: String? = null,
)
