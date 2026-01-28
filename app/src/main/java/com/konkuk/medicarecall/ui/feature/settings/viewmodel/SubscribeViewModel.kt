package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class SubscribeUiState(
    val subscriptions: List<EldersSubscriptionResponseDto> = emptyList(),
    val errorMessage: String? = null,
)

@KoinViewModel
class SubscribeViewModel(
    private val repository: SubscribeRepository,
) : ViewModel() {
    var uiState by mutableStateOf(SubscribeUiState())
        private set

    init {
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        Log.d("SubscribeViewModel", "loadSubscriptions() 진입")
        viewModelScope.launch {
            repository.getSubscriptions()
                .onSuccess {
                    Log.d("SubscribeViewModel", "구독 정보 불러오기 성공: ${it.size}개")
                    uiState = uiState.copy(subscriptions = it)
                }
                .onFailure {
                    uiState = uiState.copy(errorMessage = "구독 정보를 불러오지 못했습니다.")
                    it.printStackTrace()
                    Log.e("SubscribeViewModel", "구독 로딩 실패: ${it.message}", it)
                }
        }
    }
}
