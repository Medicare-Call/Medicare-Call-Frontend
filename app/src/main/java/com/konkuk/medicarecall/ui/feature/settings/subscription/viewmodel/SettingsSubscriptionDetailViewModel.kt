package com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel

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
class SettingsSubscriptionDetailViewModel(
    private val subscribeRepository: SubscribeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsSubscriptionDetailUiState())
    val uiState: StateFlow<SettingsSubscriptionDetailUiState> = _uiState.asStateFlow()

    fun loadSubscriptionById(elderId: Long) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            subscribeRepository.getSubscriptions()
                .onSuccess { list ->
                    val subscription = list.firstOrNull { it.elderId == elderId }
                    _uiState.update {
                        it.copy(
                            subscriptionData = subscription,
                            errorMessage = if (subscription == null) "구독 정보를 찾을 수 없습니다" else null,
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(errorMessage = "구독 정보를 불러오지 못했습니다: ${exception.message}") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
