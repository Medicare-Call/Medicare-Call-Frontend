package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailHealthViewModel @Inject constructor(
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {
    fun updateElderHealth(
        healthInfo: EldersHealthResponseDto,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            eldersHealthInfoRepository.updateHealthInfo(healthInfo)
                .onSuccess {
                    Log.d("DetailHealthViewModel", "건강 정보 수정 성공: $it")
                    onComplete?.invoke()
                }
                .onFailure { exception ->
                    Log.e("DetailHealthViewModel", "건강 정보 수정 실패: ${exception.message}", exception)
                }
        }
    }
}
