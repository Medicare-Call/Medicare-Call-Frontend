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

    var elderNameIdMapList = elderIdRepository.getElderIds()

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        ensureLoaded()
    }

    fun ensureLoaded() {
        if (eldersInfoList.isEmpty() && !isLoading.value) {
            loadEldersInfo()
        }
    }

    fun refresh() = loadEldersInfo(force = true)

    private fun loadEldersInfo(force: Boolean = false) {
        if (isLoading.value) return
        isLoading.value = true
        Log.d("EldersInfoViewModel", "loadEldersInfo() 호출 (force=$force)")

        viewModelScope.launch {
            eldersInfoRepository.getElders()
                .onSuccess { list ->
                    Log.d("EldersInfoViewModel", "노인 개인 정보 불러오기 성공: ${list.size}개")
                    eldersInfoList = list

                    // 이름→ID 매핑(순서 유지)
                    val mapped = list.map { mapOf(it.name to it.elderId) }
                    elderNameIdMapList = mapped

                    // ElderIdRepository 동기화
                    // NOTE: 중복 적재를 피하려면 ElderIdRepository에 replaceAll(...)을 추가하는 걸 추천.
                    val repoCurrent = elderIdRepository.getElderIds()
                    if (force || repoCurrent.isEmpty()) {
                        // 간단 동기화(초기 1회 or refresh 시)
                        mapped.forEach { m ->
                            val e = m.entries.first()
                            elderIdRepository.addElderId(e.key, e.value)
                        }
                    }

                    error.value = null
                    errorMessage = null
                }
                .onFailure {
                    error.value = it
                    errorMessage = "노인 개인 정보를 불러오지 못했습니다."
                    Log.e("EldersInfoViewModel", "노인 개인 정보 로딩 실패: ${it.message}", it)
                }

            isLoading.value = false
        }
    }
}
