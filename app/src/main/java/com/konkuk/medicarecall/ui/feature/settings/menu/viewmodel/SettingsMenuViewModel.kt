package com.konkuk.medicarecall.ui.feature.settings.menu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsMenuViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun refresh() = getUserData()

    private val _uiState = MutableStateFlow(SettingsMenuUiState())
    val uiState: StateFlow<SettingsMenuUiState> = _uiState.asStateFlow()

    init {
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            userRepository.getMyInfo()
                .onSuccess { data ->
                    _uiState.update { it.copy(myInfo = data) }
                }
                .onFailure {
                    Log.e("MyDataViewModel", "사용자 정보 불러오기 실패: ${it.message}", it)
                    it.printStackTrace()
                    Log.e("MyDataViewModel", "사용자 정보 로딩 실패: ${it.message}", it)
                }
        }
    }
}

