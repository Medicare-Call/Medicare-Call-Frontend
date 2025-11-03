package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EldersHealthViewModel @Inject constructor(
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {

    var eldersInfoList by mutableStateOf<List<EldersHealthResponseDto>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadEldersHealthInfo()
    }

    fun refresh() = loadEldersHealthInfo()

    private fun loadEldersHealthInfo() {
        Log.d("EldersHealthViewModel", "loadEldersHealthInfo() 진입")
        viewModelScope.launch {
            eldersHealthInfoRepository.getEldersHealthInfo()
                .onSuccess {
                    Log.d("EldersHealthViewModel", "건강 정보 불러오기 성공: ${it.size}개")
                    eldersInfoList = it
                    Log.d("EldersHealthViewModel", "노인 건강 정보: $eldersInfoList")
                }
                .onFailure { exception ->
                    errorMessage = "건강 정보를 불러오지 못했습니다: ${exception.message}"
                    exception.printStackTrace()
                    Log.e("EldersHealthViewModel", "건강 정보 로딩 실패: ${exception.message}", exception)
                }
        }
    }
}
