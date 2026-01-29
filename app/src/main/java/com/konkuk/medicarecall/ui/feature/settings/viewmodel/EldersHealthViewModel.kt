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
class EldersHealthViewModel(
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {

    private val _eldersInfoList = MutableStateFlow<List<EldersHealthResponseDto>>(emptyList())
    val eldersInfoList: StateFlow<List<EldersHealthResponseDto>> = _eldersInfoList.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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
                    _eldersInfoList.value = it
                    Log.d("EldersHealthViewModel", "노인 건강 정보: ${_eldersInfoList.value}")
                }
                .onFailure { exception ->
                    _errorMessage.value = "건강 정보를 불러오지 못했습니다: ${exception.message}"
                    exception.printStackTrace()
                    Log.e("EldersHealthViewModel", "건강 정보 로딩 실패: ${exception.message}", exception)
                }
        }
    }
}
