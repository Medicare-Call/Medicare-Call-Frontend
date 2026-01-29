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
class NoticeViewModel(
    private val repository: NoticeRepository,
) : ViewModel() {
    private val _noticeList = MutableStateFlow<List<NoticesResponseDto>>(emptyList())
    val noticeList: StateFlow<List<NoticesResponseDto>> = _noticeList.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadNotices()
    }

    private fun loadNotices() {
        Log.d("NoticeViewModel", "loadNotices() 진입")
        viewModelScope.launch {
            repository.getNotices()
                .onSuccess {
                    Log.d("NoticeViewModel", "공지사항 불러오기 성공: ${it.size}개")
                    _noticeList.value = it
                }
                .onFailure {
                    _errorMessage.value = "공지사항을 불러오지 못했습니다."
                    it.printStackTrace()
                    Log.e("NoticeViewModel", "공지 로딩 실패: ${it.message}", it)
                }
        }
    }
}
