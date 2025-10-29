package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class DetailMyDataViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
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
