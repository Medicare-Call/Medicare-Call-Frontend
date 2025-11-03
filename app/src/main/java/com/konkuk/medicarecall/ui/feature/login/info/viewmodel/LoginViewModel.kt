package com.konkuk.medicarecall.ui.feature.login.info.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.MemberRegisterRepository
import com.konkuk.medicarecall.data.repository.VerificationRepository
import com.konkuk.medicarecall.domain.usecase.CheckLoginStatusUseCase
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.ui.model.NavigationDestination
import com.konkuk.medicarecall.ui.type.GenderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val verificationRepository: VerificationRepository,
    private val memberRegisterRepository: MemberRegisterRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
) : ViewModel() {
    private val _navigationDestination = MutableStateFlow<NavigationDestination?>(null)
    val navigationDestination = _navigationDestination.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    // 휴대폰 번호 입력 value
    var phoneNumber by mutableStateOf("")
        private set

    // 인증번호 입력 value
    var verificationCode by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set
    var dateOfBirth by mutableStateOf("")
        private set
    var isMale by mutableStateOf<Boolean>(true)
        private set

    // 상태 변경 함수
    fun onPhoneNumberChanged(new: String) {
        phoneNumber = new
    }

    fun onVerificationCodeChanged(new: String) {
        verificationCode = new
    }

    fun onNameChanged(new: String) {
        name = new
    }

    fun onDOBChanged(new: String) {
        dateOfBirth = new
    }

    fun onGenderChanged(new: Boolean) {
        isMale = new
    }

    // 서버 통신 함수
    private val debug = false
    fun postPhoneNumber(phone: String) {
        if (!debug) {
            viewModelScope.launch {
                verificationRepository.requestCertificationCode(phone).fold(
                    onSuccess = {
                        Log.d("httplog", "성공, ${it.message()}")
                    },
                    onFailure = { error ->
                        Log.d("httplog", "실패, ${error.message}")
                    },
                )
            }
        }
    }

    var isVerified = false
    var token = ""
        private set

    fun confirmPhoneNumber(phone: String, code: String) {
        viewModelScope.launch {
            if (!debug) {
                verificationRepository.confirmPhoneNumber(phone, code)
                    .onSuccess {
                        Log.d(
                            "httplog",
                            "${it.message} ${it.memberStatus} 액세스토큰: ${it.accessToken} 리프레시 토큰: ${it.refreshToken} ${it.verified} ${it.token} ",
                        )
                        isVerified = it.verified
                        if (isVerified) {
                            token = it.token ?: ""
                            dataStoreRepository.saveAccessToken(it.accessToken ?: "")
                            dataStoreRepository.saveRefreshToken(it.refreshToken ?: "")
                        }
                        if (it.memberStatus == "EXISTING_MEMBER")
                            _events.emit(LoginEvent.VerificationSuccessExisting)
                        else
                            _events.emit(LoginEvent.VerificationSuccessNew)
                    }
                    .onFailure { error ->
                        Log.d("httplog", "실패, ${error.message}")
                        _events.emit(LoginEvent.VerificationFailure)
                    }
            } else {
                _events.emit(LoginEvent.VerificationSuccessNew)
            }
        }
    }

    fun memberRegister(name: String, birthDate: String, gender: GenderType) {
        viewModelScope.launch {
            if (!debug) {
                memberRegisterRepository.registerMember(
                    token,
                    name,
                    birthDate.formatAsDate(),
                    gender,
                )
                    .onSuccess {
                        // 성공 로직
                        // responseDto에는 MemberRegisterResponseDto 객체가 들어있음
                        // 예: 성공 메시지 표시, 다음 화면으로 이동
                        Log.d("httplog", "성공, ${it.accessToken} ${it.refreshToken}")
                        dataStoreRepository.saveAccessToken(it.accessToken)
                        dataStoreRepository.saveRefreshToken(it.refreshToken)
                        _events.emit(LoginEvent.MemberRegisterSuccess)
                    }
                    .onFailure { exception ->
                        // 실패 로직
                        // exception에는 API 호출 중 발생한 예외가 들어있음
                        // 예: 에러 메시지 표시
                        Log.e("httplog", "회원가입 실패: ${exception.message}")
                        _events.emit(LoginEvent.MemberRegisterFailure)
                    }
            } else {
                _events.emit(LoginEvent.MemberRegisterSuccess)
            }
        }
    }

    fun checkStatus() {
        viewModelScope.launch {
            val destination = checkLoginStatusUseCase()
            _navigationDestination.value = destination
        }
    }

    fun onNavigationHandled() {
        _navigationDestination.value = null
    }
}
