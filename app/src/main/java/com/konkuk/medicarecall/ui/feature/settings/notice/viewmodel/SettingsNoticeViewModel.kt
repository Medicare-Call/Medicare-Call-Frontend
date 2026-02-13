package com.konkuk.medicarecall.ui.feature.settings.notice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.NoticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsNoticeViewModel(
    private val repository: NoticeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsNoticeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadNotices()
    }

    private fun loadNotices() {
        viewModelScope.launch {
            repository.getNotices()
                .onSuccess { notices ->
                    _uiState.update { it.copy(noticeList = notices) }
                }
                .onFailure {
                    _uiState.update { it.copy(errorMessage = "공지사항을 불러오지 못했습니다.") }
                    it.printStackTrace()
                }
        }
    }
}
