package com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsEldersHealthViewModel(
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsEldersHealthUiState())
    val uiState: StateFlow<SettingsEldersHealthUiState> = _uiState.asStateFlow()

    init {
        loadEldersHealthInfo()
    }

    fun refresh() = loadEldersHealthInfo()

    private fun loadEldersHealthInfo() {
        viewModelScope.launch {
            eldersHealthInfoRepository.getEldersHealthInfo()
                .onSuccess {
                    _uiState.update { state -> state.copy(eldersInfoList = it) }
                }
                .onFailure { exception ->
                    _uiState.update { state ->
                        state.copy(errorMessage = "건강 정보를 불러오지 못했습니다: ${exception.message}")
                    }
                }
        }
    }
}
