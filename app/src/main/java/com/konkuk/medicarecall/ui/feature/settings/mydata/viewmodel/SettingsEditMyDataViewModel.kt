package com.konkuk.medicarecall.ui.feature.settings.mydata.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.UserRepository
import com.konkuk.medicarecall.ui.model.MyInfo
import com.konkuk.medicarecall.ui.type.GenderType
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
                        Log.e("SettingsEditMyDataViewModel", "내 정보 로딩 실패", exception)
                    }
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "내 정보를 불러오지 못했습니다: ${e.message}")
                }
                Log.e("SettingsEditMyDataViewModel", "내 정보 로딩 실패", e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateUserData(
        userInfo: MyInfo,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isUpdateSuccess = false) }
            try {
                userRepository.updateMyInfo(userInfo)
                    .onSuccess {
                        Log.d("SettingsEditMyDataViewModel", "사용자 정보 업데이트 성공: $it")
                        _uiState.update { it.copy(isUpdateSuccess = true) }
                        onComplete?.invoke()
                    }
                    .onFailure { e ->
                        if (e is CancellationException) {
                            Log.d("SettingsEditMyDataViewModel", "업데이트 취소됨: ${e.message}")
                            throw e
                        } else {
                            Log.e("SettingsEditMyDataViewModel", "사용자 정보 업데이트 실패: ${e.message}", e)
                            _uiState.update { it.copy(errorMessage = "정보 업데이트에 실패했습니다.") }
                        }
                    }
            } catch (ce: CancellationException) {
                Log.d("SettingsEditMyDataViewModel", "job cancelled(normal): ${ce.message}")
                throw ce
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // 화면 진입 시나 필요 시 상태 초기화
    fun resetStatus() {
        _uiState.update { it.copy(isUpdateSuccess = false, errorMessage = null) }
    }

    fun initializeNotificationSettings(myDataInfo: MyInfo) {
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

    fun initializeFormData(myDataInfo: MyInfo) {
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
