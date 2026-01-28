package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailHealthViewModel(
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {
    private val _healthData = MutableStateFlow<EldersHealthResponseDto?>(null)
    val healthData: StateFlow<EldersHealthResponseDto?> = _healthData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadHealthInfoById(elderId: Int) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            eldersHealthInfoRepository.getEldersHealthInfo()
                .onSuccess { list ->
                    val elderHealth = list.firstOrNull { it.elderId == elderId }
                    _healthData.value = elderHealth
                    if (elderHealth == null) {
                        _errorMessage.value = "건강 정보를 찾을 수 없습니다"
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = "건강 정보를 불러오지 못했습니다: ${exception.message}"
                    Log.e("DetailHealthViewModel", "건강 정보 로딩 실패", exception)
                }
            _isLoading.value = false
        }
    }

    fun updateElderHealth(
        healthInfo: EldersHealthResponseDto,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            eldersHealthInfoRepository.updateHealthInfo(healthInfo)
                .onSuccess {
                    Log.d("DetailHealthViewModel", "건강 정보 수정 성공: $it")
                    loadHealthInfoById(healthInfo.elderId)
                    onComplete?.invoke()
                }
                .onFailure { exception ->
                    Log.e("DetailHealthViewModel", "건강 정보 수정 실패: ${exception.message}", exception)
                }
        }
    }
}
