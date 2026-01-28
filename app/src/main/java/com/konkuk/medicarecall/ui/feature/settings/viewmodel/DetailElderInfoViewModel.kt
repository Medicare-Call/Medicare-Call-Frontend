package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailElderInfoViewModel(
    private val eldersInfoRepository: EldersInfoRepository,
    private val updateElderInfoRepository: UpdateElderInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<EldersInfoResponseDto?>(null)
    val uiState: StateFlow<EldersInfoResponseDto?> = _uiState.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    fun loadElderDataById(elderId: Int) {
        viewModelScope.launch {
            eldersInfoRepository.getElders()
                .onSuccess { list ->
                    val elderData = list.firstOrNull { it.elderId == elderId }
                    _uiState.value = elderData
                    if (elderData == null) {
                        Log.w("DetailElderInfoViewModel", "어르신 정보를 찾을 수 없습니다. elderId: $elderId")
                    }
                }
                .onFailure { exception ->
                    Log.e("DetailElderInfoViewModel", "어르신 정보 로딩 실패", exception)
                }
        }
    }

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
        Log.d("DetailElderInfoViewModel", "어르신 개인 정보 수정 요청: $updateInfo")
        viewModelScope.launch {
            updateElderInfoRepository.updateElderInfo(
                id = elderInfo.elderId,
                request = updateInfo,
            )
                .onSuccess {
                    Log.d("DetailElderInfoViewModel", "어르신 개인 정보 수정 완료: $it")
                    _isSuccess.value = true
                    loadElderDataById(elderInfo.elderId)
                    onComplete?.invoke()
                }
                .onFailure { exception ->
                    Log.e("DetailElderInfoViewModel", "어르신 개인 정보 수정 실패: $exception")
                    _isSuccess.value = false
                }
        }
    }

    fun deleteElderInfo(elderId: Int) {
        Log.d("DetailElderInfoViewModel", "어르신 정보 삭제 요청: ID = $elderId")
        viewModelScope.launch {
            updateElderInfoRepository.deleteElder(elderId)
        }
    }
}
