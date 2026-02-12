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
class SettingsNoticeDetailViewModel(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsNoticeDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun loadNoticeById(noticeId: Long) {
        _uiState.update { it.copy(isLoading = true) }
        _uiState.update { it.copy(errorMessage = null) }
        viewModelScope.launch {
            noticeRepository.getNotices()
                .onSuccess { list ->
                    val notice = list.firstOrNull { it.id == noticeId }
                    _uiState.update { it.copy(noticeData = notice) }
                    if (notice == null) {
                        _uiState.update { it.copy(errorMessage = "공지사항을 찾을 수 없습니다") }
                    }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(errorMessage = "공지사항을 불러오지 못했습니다: ${exception.message}") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
