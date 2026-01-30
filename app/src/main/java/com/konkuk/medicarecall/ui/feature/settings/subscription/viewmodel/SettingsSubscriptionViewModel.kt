package com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsSubscriptionViewModel(
    private val repository: SubscribeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsSubscriptionUiState())
    val uiState: StateFlow<SettingsSubscriptionUiState> = _uiState.asStateFlow()

    init {
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        Log.d("SettingsSubscriptionViewModel", "loadSubscriptions() 진입")
        viewModelScope.launch {
            repository.getSubscriptions()
                .onSuccess {
                    Log.d("SettingsSubscriptionViewModel", "구독 정보 불러오기 성공: ${it.size}개")
                    _uiState.update { state -> state.copy(subscriptions = it) }
                }
                .onFailure {
                    _uiState.update { state -> state.copy(errorMessage = "구독 정보를 불러오지 못했습니다.") }
                    it.printStackTrace()
                    Log.e("SettingsSubscriptionViewModel", "구독 로딩 실패: ${it.message}", it)
                }
        }
    }
}
