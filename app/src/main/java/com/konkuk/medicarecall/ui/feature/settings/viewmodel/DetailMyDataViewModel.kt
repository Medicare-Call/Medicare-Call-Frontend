package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.coroutines.cancellation.CancellationException

@KoinViewModel
class DetailMyDataViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _myDataInfo = MutableStateFlow<MyInfoResponseDto?>(null)
    val myDataInfo: StateFlow<MyInfoResponseDto?> = _myDataInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadMyData()
    }

    fun loadMyData() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            userRepository.getMyInfo()
                .onSuccess { myInfo ->
                    _myDataInfo.value = myInfo
                }
                .onFailure { exception ->
                    _errorMessage.value = "내 정보를 불러오지 못했습니다: ${exception.message}"
                    Log.e("DetailMyDataViewModel", "내 정보 로딩 실패", exception)
                }
            _isLoading.value = false
        }
    }

    fun updateUserData(
        userInfo: MyInfoResponseDto,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                val result = userRepository.updateMyInfo(userInfo)
                result
                    .onSuccess {
                        Log.d("DetailMyDataViewModel", "사용자 정보 업데이트 성공: $it")
                        loadMyData()
                        onComplete?.invoke()
                    }
                    .onFailure { e ->
                        if (e is CancellationException) {
                            Log.d("DetailMyDataViewModel", "업데이트 취소됨: ${e.message}")
                            throw e // 취소 상태 유지
                        } else {
                            Log.e("DetailMyDataViewModel", "사용자 정보 업데이트 실패: ${e.message}", e)
                        }
                    }
            } catch (ce: CancellationException) {
                Log.d("DetailMyDataViewModel", "job cancelled(normal): ${ce.message}")
                throw ce
            }
        }
    }
}
