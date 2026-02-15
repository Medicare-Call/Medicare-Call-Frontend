package com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsEldersInfoViewModel(
    private val eldersInfoRepository: EldersInfoRepository,
    private val elderIdRepository: ElderIdRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsEldersInfoUiState())
    val uiState: StateFlow<SettingsEldersInfoUiState> = _uiState.asStateFlow()

    init {
        if (_uiState.value.eldersInfoList.isEmpty()) {
            loadEldersInfo()
        }
    }

    fun refresh() = loadEldersInfo()

    private fun loadEldersInfo() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            eldersInfoRepository.getElders()
                .onSuccess { list ->
                    _uiState.update { it.copy(eldersInfoList = list, errorMessage = null) }
                    val mapped = list.associate { it.elderId to it.name }
                    elderIdRepository.updateElderIds(mapped)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(errorMessage = "노인 개인 정보를 불러오지 못했습니다.") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
