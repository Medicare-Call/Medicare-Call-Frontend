package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SubscribeViewModel(
    private val repository: SubscribeRepository,
) : ViewModel() {
    private val _subscriptions = MutableStateFlow<List<EldersSubscriptionResponseDto>>(emptyList())
    val subscriptions: StateFlow<List<EldersSubscriptionResponseDto>> = _subscriptions.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        Log.d("SubscribeViewModel", "loadSubscriptions() 진입")
        viewModelScope.launch {
            repository.getSubscriptions()
                .onSuccess {
                    Log.d("SubscribeViewModel", "구독 정보 불러오기 성공: ${it.size}개")
                    _subscriptions.value = it
                }
                .onFailure {
                    _errorMessage.value = "구독 정보를 불러오지 못했습니다."
                    it.printStackTrace()
                    Log.e("SubscribeViewModel", "구독 로딩 실패: ${it.message}", it)
                }
        }
    }
}
