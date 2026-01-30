package com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel

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
class SettingsSubscriptionDetailViewModel(
    private val subscribeRepository: SubscribeRepository,
) : ViewModel() {
    private val _subscriptionData = MutableStateFlow<EldersSubscriptionResponseDto?>(null)
    val subscriptionData: StateFlow<EldersSubscriptionResponseDto?> = _subscriptionData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadSubscriptionById(elderId: Int) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            subscribeRepository.getSubscriptions()
                .onSuccess { list ->
                    val subscription = list.firstOrNull { it.elderId == elderId }
                    _subscriptionData.value = subscription
                    if (subscription == null) {
                        _errorMessage.value = "구독 정보를 찾을 수 없습니다"
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = "구독 정보를 불러오지 못했습니다: ${exception.message}"
                    Log.e("DetailSubscribeViewModel", "구독 정보 로딩 실패", exception)
                }
            _isLoading.value = false
        }
    }
}
