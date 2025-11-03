package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyDataViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun refresh() = getUserData()
    var myDataInfo by mutableStateOf(MyInfoResponseDto())
        private set

    init {
        getUserData()
    }

    fun logout(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            userRepository.logout()
                .onSuccess { onSuccess() }
                .onFailure { onError(it) }
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            userRepository.getMyInfo()
                .onSuccess {
                    Log.d("MyDataViewModel", "사용자 정보 불러오기 성공: $it")
                    myDataInfo = it
                }
                .onFailure {
                    Log.e("MyDataViewModel", "사용자 정보 불러오기 실패: ${it.message}", it)
                    it.printStackTrace()
                    Log.e("MyDataViewModel", "사용자 정보 로딩 실패: ${it.message}", it)
                }
        }
    }
}
