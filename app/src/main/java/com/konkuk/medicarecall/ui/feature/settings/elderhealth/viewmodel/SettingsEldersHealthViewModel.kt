package com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel

import android.util.Log
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
        Log.d("SettingsEldersHealthViewModel", "loadEldersHealthInfo() 진입")
        viewModelScope.launch {
            eldersHealthInfoRepository.getEldersHealthInfo()
                .onSuccess {
                    Log.d("SettingsEldersHealthViewModel", "건강 정보 불러오기 성공: ${it.size}개")
                    _uiState.update { state -> state.copy(eldersInfoList = it) }
                }
                .onFailure { exception ->
                    _uiState.update { state ->
                        state.copy(errorMessage = "건강 정보를 불러오지 못했습니다: ${exception.message}")
                    }
                    Log.e("SettingsEldersHealthViewModel", "건강 정보 로딩 실패: ${exception.message}", exception)
                }
        }
    }
}
