package com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.FcmRepository
import com.konkuk.medicarecall.data.repository.MemberRegisterRepository
import com.konkuk.medicarecall.data.repository.VerificationRepository
import com.konkuk.medicarecall.domain.model.Verification
import com.konkuk.medicarecall.domain.usecase.CheckLoginStatusUseCase
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.domain.model.type.GenderType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginInfoViewModel(
    private val verificationRepository: VerificationRepository,
    private val memberRegisterRepository: MemberRegisterRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
    private val fcmRepository: FcmRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginInfoUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    fun onPhoneNumberChanged(new: String) {
        _uiState.update { it.copy(userInfo = it.userInfo.copy(phoneNumber = new)) }
    }

    fun onVerificationCodeChanged(new: String) {
        _uiState.update { it.copy(verificationCode = new) }
    }

    fun onNameChanged(new: String) {
        _uiState.update { it.copy(userInfo = it.userInfo.copy(name = new)) }
    }

    fun onDOBChanged(new: String) {
        _uiState.update { it.copy(userInfo = it.userInfo.copy(birthDate = new)) }
    }

    fun onGenderChanged(new: GenderType) {
        _uiState.update { it.copy(userInfo = it.userInfo.copy(gender = new)) }
    }

    fun setShowBottomSheet(value: Boolean) {
        _uiState.update { it.copy(showBottomSheet = value) }
    }

    fun setCheckedState(index: Int, value: Boolean) {
        _uiState.update {
            it.copy(checkedStates = it.checkedStates.toMutableList().apply { set(index, value) })
        }
    }

    fun setAllAgreeCheckState(value: Boolean) {
        _uiState.update {
            it.copy(
                allAgreeCheckState = value,
                checkedStates = List(it.checkedStates.size) { value },
            )
        }
    }

    fun updateAllCheckedStates(newStates: List<Boolean>) {
        _uiState.update { it.copy(checkedStates = newStates) }
    }

    private val debug = false

    /** 인증번호 요청 */
    fun postPhoneNumber(phone: String) {
        if (!debug) {
            viewModelScope.launch {
                verificationRepository.requestCertificationCode(phone)
                    .onSuccess { Log.d("httplog", "인증번호 요청 성공, ${it.message}") }
                    .onFailure { Log.e("httplog", "인증번호 요청 실패: ${it.message}") }
            }
        }
    }

    /** 인증번호 확인 및 로그인 처리 */
    fun confirmPhoneNumber(phone: String, code: String) {
        viewModelScope.launch {
            if (!debug) {
                verificationRepository.confirmPhoneNumber(phone, code)
                    .onSuccess { data ->
                        if (data.verified) {
                            when (data) {
                                is Verification.NewMember -> {
                                    dataStoreRepository.saveAccessToken(data.newUserToken)
                                    _events.emit(LoginEvent.VerificationSuccessNew)
                                }

                                is Verification.ExistingMember -> {
                                    fcmRepository.validateAndRefreshTokenIfNeeded(data.accessToken)
                                    dataStoreRepository.saveAccessToken(data.accessToken)
                                    dataStoreRepository.saveRefreshToken(data.refreshToken)
                                    _events.emit(LoginEvent.VerificationSuccessExisting)
                                }
                            }
                        } else {
                            _events.emit(LoginEvent.VerificationFailure)
                        }
                    }
                    .onFailure { error ->
                        Log.e("httplog", "로그인 실패: ${error.message}")
                        _events.emit(LoginEvent.VerificationFailure)
                    }
            } else {
                _events.emit(LoginEvent.VerificationSuccessNew)
            }
        }
    }

    /** 회원가입 */
    fun memberRegister() {
        viewModelScope.launch {
            try {
                if (debug) {
                    _events.emit(LoginEvent.MemberRegisterSuccess)
                    return@launch
                }

                val userInfo = _uiState.value.userInfo
                val fcmToken = FirebaseMessaging.getInstance().token.await()
                Log.d("httplog", "회원가입 시 FCM 토큰 사용: $fcmToken")

                memberRegisterRepository.registerMember(
                    name = userInfo.name,
                    birthDate = userInfo.birthDate.formatAsDate(),
                    gender = userInfo.gender,
                    fcmToken = fcmToken,
                ).onSuccess {
                    Log.d("httplog", "회원가입 성공: ${it.accessToken} ${it.refreshToken}")
                    dataStoreRepository.saveAccessToken(it.accessToken)
                    dataStoreRepository.saveRefreshToken(it.refreshToken)
                    _events.emit(LoginEvent.MemberRegisterSuccess)
                }.onFailure { e ->
                    Log.e("httplog", "회원가입 실패: ${e.message}")
                    _events.emit(LoginEvent.MemberRegisterFailure)
                }
            } catch (e: Exception) {
                Log.e("httplog", "회원가입 중 예외 발생: ${e.message}")
                _events.emit(LoginEvent.MemberRegisterFailure)
            }
        }
    }

    /** 로그인 상태 확인 */
    fun checkStatus() {
        viewModelScope.launch {
            val destination = checkLoginStatusUseCase()
            _uiState.update { it.copy(navigationDestination = destination) }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationDestination = null) }
    }
}
