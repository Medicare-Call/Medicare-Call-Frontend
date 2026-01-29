package com.konkuk.medicarecall.ui.feature.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.repository.UserRepository
import com.konkuk.medicarecall.ui.type.GenderType
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
    // Notification state (for SettingAlarmScreen)
    private val _masterChecked = MutableStateFlow(false)
    val masterChecked: StateFlow<Boolean> = _masterChecked.asStateFlow()

    private val _completeChecked = MutableStateFlow(false)
    val completeChecked: StateFlow<Boolean> = _completeChecked.asStateFlow()

    private val _abnormalChecked = MutableStateFlow(false)
    val abnormalChecked: StateFlow<Boolean> = _abnormalChecked.asStateFlow()

    private val _missedChecked = MutableStateFlow(false)
    val missedChecked: StateFlow<Boolean> = _missedChecked.asStateFlow()

    // Form state (for MyDetailScreen)
    private val _isMale = MutableStateFlow(false)
    val isMale: StateFlow<Boolean> = _isMale.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _birth = MutableStateFlow("")
    val birth: StateFlow<String> = _birth.asStateFlow()

    // 사용자 정보 상태
    private val _myDataInfo = MutableStateFlow<MyInfoResponseDto?>(null)
    val myDataInfo: StateFlow<MyInfoResponseDto?> = _myDataInfo.asStateFlow()

    // Async state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _isUpdateSuccess = MutableStateFlow(false)
    val isUpdateSuccess: StateFlow<Boolean> = _isUpdateSuccess.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // API 호출 함수
    fun loadMyInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                            userRepository.getMyInfo()
                                .onSuccess { myInfo ->
                                        _myDataInfo.value = myInfo
                                    }
                                .onFailure { exception ->
                                        _errorMessage.value = "내 정보를 불러오지 못했습니다: ${exception.message}"
                                        Log.e("DetailMyDataViewModel", "내 정보 로딩 실패", exception)
                                    }
                        } catch (ce: CancellationException) {
                            throw ce
                       } catch (e: Exception) {
                            _errorMessage.value = "내 정보를 불러오지 못했습니다: ${e.message}"
                            Log.e("DetailMyDataViewModel", "내 정보 로딩 실패", e)
                        } finally {
                            _isLoading.value = false
                        }
        }
    }

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

    fun initializeNotificationSettings(myDataInfo: MyInfoResponseDto) {
        _masterChecked.value = myDataInfo.pushNotification.all == "ON"
        _completeChecked.value = myDataInfo.pushNotification.carecallCompleted == "ON" || _masterChecked.value
        _abnormalChecked.value = myDataInfo.pushNotification.healthAlert == "ON" || _masterChecked.value
        _missedChecked.value = myDataInfo.pushNotification.carecallMissed == "ON" || _masterChecked.value
    }

    fun setMasterChecked(value: Boolean) {
        _masterChecked.value = value
        _completeChecked.value = value
        _abnormalChecked.value = value
        _missedChecked.value = value
    }

    fun setCompleteChecked(value: Boolean) {
        _completeChecked.value = value
        if (!value) {
            _masterChecked.value = false
        }
    }

    fun setAbnormalChecked(value: Boolean) {
        _abnormalChecked.value = value
        if (!value) {
            _masterChecked.value = false
        }
    }

    fun setMissedChecked(value: Boolean) {
        _missedChecked.value = value
        if (!value) {
            _masterChecked.value = false
        }
    }

    fun initializeFormData(myDataInfo: MyInfoResponseDto) {
        _isMale.value = myDataInfo.gender == GenderType.MALE
        _name.value = myDataInfo.name
        _birth.value = myDataInfo.birthDate.replace("-", "")
    }

    fun updateIsMale(value: Boolean) {
        _isMale.value = value
    }

    fun updateName(value: String) {
        _name.value = value
    }

    fun updateBirth(value: String) {
        _birth.value = value
    }
}
