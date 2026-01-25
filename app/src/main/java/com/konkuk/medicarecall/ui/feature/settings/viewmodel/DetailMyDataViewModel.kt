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
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _isUpdateSuccess = MutableStateFlow(false)
    val isUpdateSuccess: StateFlow<Boolean> = _isUpdateSuccess.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun updateUserData(
        userInfo: MyInfoResponseDto,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isUpdateSuccess.value = false
            try {
                userRepository.updateMyInfo(userInfo)
                    .onSuccess {
                        Log.d("DetailMyDataViewModel", "사용자 정보 업데이트 성공: $it")
                        _isUpdateSuccess.value = true
                        onComplete?.invoke()
                    }
                    .onFailure { e ->
                        if (e is CancellationException) {
                            Log.d("DetailMyDataViewModel", "업데이트 취소됨: ${e.message}")
                            throw e // 취소 상태 유지
                        } else {
                            Log.e("DetailMyDataViewModel", "사용자 정보 업데이트 실패: ${e.message}", e)
                            _errorMessage.value = "정보 업데이트에 실패했습니다."
                        }
                    }
            } catch (ce: CancellationException) {
                Log.d("DetailMyDataViewModel", "job cancelled(normal): ${ce.message}")
                throw ce
            } finally {
                // 작업 완료 후 로딩 해제
                _isLoading.value = false
            }
        }
    }

    // 화면 진입 시나 필요 시 상태 초기화
    fun resetStatus() {
        _isUpdateSuccess.value = false
        _errorMessage.value = null
    }
}
