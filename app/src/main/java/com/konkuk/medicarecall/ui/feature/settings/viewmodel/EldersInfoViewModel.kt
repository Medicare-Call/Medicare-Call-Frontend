package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EldersInfoViewModel(
    private val eldersInfoRepository: EldersInfoRepository,
    private val elderIdRepository: ElderIdRepository,
) : ViewModel() {

    private val _eldersInfoList = MutableStateFlow<List<EldersInfoResponseDto>>(emptyList())
    val eldersInfoList: StateFlow<List<EldersInfoResponseDto>> = _eldersInfoList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> = _error.asStateFlow()
    private val _elderNameIdMapList = MutableStateFlow(elderIdRepository.getElderIds())
    val elderNameIdMapList: StateFlow<List<Map<String, Int>>> = _elderNameIdMapList.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        ensureLoaded()
    }

    fun ensureLoaded() {
        if (_eldersInfoList.value.isEmpty() && !_isLoading.value) {
            loadEldersInfo()
        }
    }

    fun refresh() = loadEldersInfo(force = true)

    private fun loadEldersInfo(force: Boolean = false) {
        if (_isLoading.value) return
        _isLoading.value = true
        Log.d("EldersInfoViewModel", "loadEldersInfo() 호출 (force=$force)")

        viewModelScope.launch {
            eldersInfoRepository.getElders()
                .onSuccess { list ->
                    Log.d("EldersInfoViewModel", "노인 개인 정보 불러오기 성공: ${list.size}개")
                    _eldersInfoList.value = list

                    // 이름→ID 매핑(순서 유지)
                    val mapped = list.map { mapOf(it.name to it.elderId) }
                    _elderNameIdMapList.value = mapped

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

                    _error.value = null
                    _errorMessage.value = null
                }
                .onFailure {
                    _error.value = it
                    _errorMessage.value = "노인 개인 정보를 불러오지 못했습니다."
                    Log.e("EldersInfoViewModel", "노인 개인 정보 로딩 실패: ${it.message}", it)
                }

            _isLoading.value = false
        }
    }
}
