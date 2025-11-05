package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.data.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val repository: NoticeRepository,
) : ViewModel() {

    var noticeList by mutableStateOf<List<NoticesResponseDto>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadNotices()
    }

    private fun loadNotices() {
        Log.d("NoticeViewModel", "loadNotices() 진입")
        viewModelScope.launch {
            repository.getNotices()
                .onSuccess {
                    Log.d("NoticeViewModel", "공지사항 불러오기 성공: ${it.size}개")
                    noticeList = it
                }
                .onFailure {
                    errorMessage = "공지사항을 불러오지 못했습니다."
                    it.printStackTrace()
                    Log.e("NoticeViewModel", "공지 로딩 실패: ${it.message}", it)
                }
        }
    }
}
