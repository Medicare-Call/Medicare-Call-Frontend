package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EldersInfoViewModel @Inject constructor(
    private val eldersInfoRepository: EldersInfoRepository,
    private val elderIdRepository: ElderIdRepository,
) : ViewModel() {
    var eldersInfoList by mutableStateOf<List<EldersInfoResponseDto>>(emptyList())
        private set

    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<Throwable?>(null)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        if (eldersInfoList.isEmpty()) {
            loadEldersInfo()
        }
    }

    fun refresh() = loadEldersInfo()

    private fun loadEldersInfo() {
        if (isLoading.value) return
        isLoading.value = true

        viewModelScope.launch {
            eldersInfoRepository.getElders()
                .onSuccess { list ->
                    eldersInfoList = list

                    // 서버 → DataStore 전체 동기화
                    val mapped = list.associate { it.elderId to it.name }
                    elderIdRepository.updateElderIds(mapped)

                    error.value = null
                    errorMessage = null
                }
                .onFailure {
                    error.value = it
                    errorMessage = "노인 개인 정보를 불러오지 못했습니다."
                    Log.e("EldersInfoViewModel", "load 실패", it)
                }
            isLoading.value = false
        }
    }
}
