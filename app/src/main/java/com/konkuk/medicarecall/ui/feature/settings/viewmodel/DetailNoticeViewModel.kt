package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.data.repository.NoticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailNoticeViewModel(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {
    private val _noticeData = MutableStateFlow<NoticesResponseDto?>(null)
    val noticeData: StateFlow<NoticesResponseDto?> = _noticeData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadNoticeById(noticeId: Int) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            noticeRepository.getNotices()
                .onSuccess { list ->
                    val notice = list.firstOrNull { it.id == noticeId }
                    _noticeData.value = notice
                    if (notice == null) {
                        _errorMessage.value = "공지사항을 찾을 수 없습니다"
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = "공지사항을 불러오지 못했습니다: ${exception.message}"
                    Log.e("DetailNoticeViewModel", "공지사항 로딩 실패", exception)
                }
            _isLoading.value = false
        }
    }
}
