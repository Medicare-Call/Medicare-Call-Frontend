package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class EldersHealthUiState(
    val eldersInfoList: List<EldersHealthResponseDto> = emptyList(),
    val errorMessage: String? = null,
)

@KoinViewModel
class EldersHealthViewModel(
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {

    var uiState by mutableStateOf(EldersHealthUiState())
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
                    uiState = uiState.copy(eldersInfoList = it)
                    Log.d("EldersHealthViewModel", "노인 건강 정보: $uiState")
                }
                .onFailure { exception ->
                    uiState = uiState.copy(
                        errorMessage = "건강 정보를 불러오지 못했습니다: ${exception.message}"
                    )
                    exception.printStackTrace()
                    Log.e("EldersHealthViewModel", "건강 정보 로딩 실패: ${exception.message}", exception)
                }
        }
    }
}
