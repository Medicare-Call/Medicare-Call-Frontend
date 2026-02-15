package com.konkuk.medicarecall.ui.feature.settings.mydata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.UserRepository
import com.konkuk.medicarecall.domain.model.UserInfo
import com.konkuk.medicarecall.domain.model.type.GenderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.coroutines.cancellation.CancellationException

@KoinViewModel
class SettingsEditMyDataViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsEditMyDataUiState())
    val uiState: StateFlow<SettingsEditMyDataUiState> = _uiState.asStateFlow()

    // API 호출 함수
    fun loadMyInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                userRepository.getMyInfo()
                    .onSuccess { myInfo ->
                        _uiState.update { it.copy(myDataInfo = myInfo) }
                    }
                    .onFailure { exception ->
                        _uiState.update {
                            it.copy(errorMessage = "내 정보를 불러오지 못했습니다: ${exception.message}")
                        }
                    }
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "내 정보를 불러오지 못했습니다: ${e.message}")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateUserData(
        userInfo: UserInfo,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isUpdateSuccess = false) }
            try {
                userRepository.updateMyInfo(userInfo)
                    .onSuccess {
                        _uiState.update { it.copy(isUpdateSuccess = true) }
                        onComplete?.invoke()
                    }
                    .onFailure { e ->
                        if (e is CancellationException) {
                            throw e
                        } else {
                            _uiState.update { it.copy(errorMessage = "정보 업데이트에 실패했습니다.") }
                        }
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // 화면 진입 시나 필요 시 상태 초기화
    fun resetStatus() {
        _uiState.update { it.copy(isUpdateSuccess = false, errorMessage = null) }
    }

    fun initializeNotificationSettings(myDataInfo: UserInfo) {
        val masterOn = myDataInfo.pushNotification.isAllEnabled
        _uiState.update {
            it.copy(
                masterChecked = masterOn,
                completeChecked = myDataInfo.pushNotification.isCarecallCompletedEnabled || masterOn,
                abnormalChecked = myDataInfo.pushNotification.isHealthAlertEnabled || masterOn,
                missedChecked = myDataInfo.pushNotification.isCarecallMissedEnabled || masterOn,
            )
        }
    }

    fun setMasterChecked(value: Boolean) {
        _uiState.update {
            it.copy(
                masterChecked = value,
                completeChecked = value,
                abnormalChecked = value,
                missedChecked = value,
            )
        }
    }

    fun setCompleteChecked(value: Boolean) {
        _uiState.update {
            it.copy(
                completeChecked = value,
                masterChecked = if (!value) false else it.masterChecked,
            )
        }
    }

    fun setAbnormalChecked(value: Boolean) {
        _uiState.update {
            it.copy(
                abnormalChecked = value,
                masterChecked = if (!value) false else it.masterChecked,
            )
        }
    }

    fun setMissedChecked(value: Boolean) {
        _uiState.update {
            it.copy(
                missedChecked = value,
                masterChecked = if (!value) false else it.masterChecked,
            )
        }
    }

    fun initializeFormData(myDataInfo: UserInfo) {
        _uiState.update {
            it.copy(
                isMale = myDataInfo.gender == GenderType.MALE,
                name = myDataInfo.name,
                birth = myDataInfo.birthDate.replace("-", ""),
            )
        }
    }

    fun updateIsMale(value: Boolean) {
        _uiState.update { it.copy(isMale = value) }
    }

    fun updateName(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun updateBirth(value: String) {
        _uiState.update { it.copy(birth = value) }
    }
}
