package com.konkuk.medicarecall.ui.feature.settings.mydata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.UserRepository
import com.konkuk.medicarecall.domain.model.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsMyDataViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun refresh() = getUserData()

    // 내부 수정용
    private val _myDataInfo = MutableStateFlow(UserInfo())

    // 외부 노출용 (읽기 전용)
    val myDataInfo: StateFlow<UserInfo> = _myDataInfo.asStateFlow()

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
                    _myDataInfo.value = it
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }
}
