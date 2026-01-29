package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailHealthViewModel(
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {

    // 1. 데이터 상태 (조회된 건강 정보)
    private val _healthData = MutableStateFlow<EldersHealthResponseDto?>(null)
    val healthData: StateFlow<EldersHealthResponseDto?> = _healthData.asStateFlow()

    // 2. UI 로딩 상태 (조회 및 수정 시 사용)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 3. 수정 성공 여부 (UI에서 화면 종료나 토스트 메시지 트리거용)
    private val _isUpdateSuccess = MutableStateFlow(false)
    val isUpdateSuccess: StateFlow<Boolean> = _isUpdateSuccess.asStateFlow()

    // 4. 에러 메시지 상태
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * 특정 어르신의 건강 정보를 불러옵니다.
     */
    fun loadHealthInfoById(elderId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

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

    /**
     * 어르신의 건강 정보를 수정하고, 성공 시 데이터를 갱신합니다.
     */
    fun updateElderHealth(
        healthInfo: EldersHealthResponseDto,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _isUpdateSuccess.value = false
            _errorMessage.value = null

            try {
                eldersHealthInfoRepository.updateHealthInfo(healthInfo)
                    .onSuccess {
                        Log.d("DetailHealthViewModel", "건강 정보 수정 성공: $it")

                        // 수정 성공 상태 업데이트
                        _isUpdateSuccess.value = true

                        // 수정된 최신 데이터로 다시 불러오기 (화면 갱신)
                        loadHealthInfoById(healthInfo.elderId)

                        onComplete?.invoke()
                    }
                    .onFailure { exception ->
                        Log.e("DetailHealthViewModel", "건강 정보 수정 실패: ${exception.message}", exception)
                        _errorMessage.value = "건강 정보 수정에 실패했습니다. 다시 시도해주세요."
                    }
            } finally {
                // 성공/실패 여부와 상관없이 로딩 해제 (안전장치)
                _isLoading.value = false
            }
        }
    }
    fun resetStatus() {
        _isUpdateSuccess.value = false
        _errorMessage.value = null
    }
}
