package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailElderInfoViewModel(
    private val eldersInfoRepository: UpdateElderInfoRepository,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isUpdateSuccess = MutableStateFlow(false)
    val isUpdateSuccess: StateFlow<Boolean> = _isUpdateSuccess.asStateFlow()

    private val _isDeleteSuccess = MutableStateFlow(false)
    val isDeleteSuccess: StateFlow<Boolean> = _isDeleteSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun updateElderInfo(
        elderInfo: EldersInfoResponseDto,
        onComplete: (() -> Unit)? = null,
    ) {
        val updateInfo = ElderRegisterRequestDto(
            name = elderInfo.name,
            birthDate = elderInfo.birthDate,
            gender = elderInfo.gender,
            phone = elderInfo.phone,
            relationship = elderInfo.relationship,
            residenceType = elderInfo.residenceType,
        )

        viewModelScope.launch {
            _isLoading.value = true
            _isUpdateSuccess.value = false
            _errorMessage.value = null
            try {
                eldersInfoRepository.updateElderInfo(
                    id = elderInfo.elderId,
                    request = updateInfo,
                ).onSuccess {
                    Log.d("DetailElderInfoViewModel", "어르신 개인 정보 수정 완료: $it")
                    _isUpdateSuccess.value = true
                    onComplete?.invoke()
                }.onFailure { exception ->
                    // 취소 예외 처리 추가
                    if (exception is CancellationException) throw exception
                    Log.e("DetailElderInfoViewModel", "어르신 개인 정보 수정 실패: $exception")
                    _errorMessage.value = "정보 수정을 실패했습니다. 다시 시도해주세요."
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteElderInfo(elderId: Int, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isDeleteSuccess.value = false
            try {
                eldersInfoRepository.deleteElder(elderId)
                    .onSuccess {
                        Log.d("DetailElderInfoViewModel", "어르신 정보 삭제 완료: $it")
                        _isDeleteSuccess.value = true
                        onComplete?.invoke()
                    }
                    .onFailure { exception ->
                        if (exception is CancellationException) throw exception
                        Log.e("DetailElderInfoViewModel", "어르신 정보 삭제 실패: $exception")
                        _errorMessage.value = "정보 삭제를 실패했습니다."
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetStatus() {
        _isUpdateSuccess.value = false
        _isDeleteSuccess.value = false
        _errorMessage.value = null
    }
}
