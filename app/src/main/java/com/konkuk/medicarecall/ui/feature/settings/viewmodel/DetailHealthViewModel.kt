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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _isUpdateSuccess = MutableStateFlow(false)
    val isUpdateSuccess: StateFlow<Boolean> = _isUpdateSuccess.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
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
                        _isUpdateSuccess.value = true
                        onComplete?.invoke()
                    }
                    .onFailure { exception ->
                        Log.e("DetailHealthViewModel", "건강 정보 수정 실패: ${exception.message}", exception)
                        _errorMessage.value = "건강 정보 수정에 실패했습니다. 다시 시도해주세요."
                    }
            } finally {
                // 성공/실패 여부와 상관없이 로딩 해제
                _isLoading.value = false
            }
        }
    }

    // UI에서 상태를 초기화할 때 사용
    fun resetStatus() {
        _isUpdateSuccess.value = false
        _errorMessage.value = null
    }
}
