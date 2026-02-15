package com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.domain.model.ElderHealthInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsElderHealthDetailViewModel(
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsElderHealthDetailUiState())
    val uiState: StateFlow<SettingsElderHealthDetailUiState> = _uiState.asStateFlow()

    fun loadHealthInfoById(elderId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            eldersHealthInfoRepository.getEldersHealthInfo()
                .onSuccess { list ->
                    val elderHealth = list.firstOrNull { it.elderId == elderId }
                    _uiState.update {
                        it.copy(
                            healthData = elderHealth,
                            errorMessage = if (elderHealth == null) "건강 정보를 찾을 수 없습니다" else null,
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(errorMessage = "건강 정보를 불러오지 못했습니다: ${exception.message}")
                    }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateElderHealth(
        healthInfo: ElderHealthInfo,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isUpdateSuccess = false, errorMessage = null) }
            try {
                eldersHealthInfoRepository.updateHealthInfo(healthInfo)
                    .onSuccess {
                        _uiState.update { it.copy(isUpdateSuccess = true) }
                        loadHealthInfoById(healthInfo.elderId)
                        onComplete?.invoke()
                    }
                    .onFailure { exception ->
                        _uiState.update { it.copy(errorMessage = "건강 정보 수정에 실패했습니다. 다시 시도해주세요.") }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetStatus() {
        _uiState.update { it.copy(isUpdateSuccess = false, errorMessage = null) }
    }
}
