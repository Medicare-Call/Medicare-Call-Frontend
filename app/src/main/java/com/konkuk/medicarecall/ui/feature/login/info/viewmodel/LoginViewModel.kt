package com.konkuk.medicarecall.ui.feature.login.info.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.FcmRepository
import com.konkuk.medicarecall.data.repository.MemberRegisterRepository
import com.konkuk.medicarecall.data.repository.VerificationRepository
import com.konkuk.medicarecall.domain.usecase.CheckLoginStatusUseCase
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.ui.model.NavigationDestination
import com.konkuk.medicarecall.ui.type.GenderType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val verificationRepository: VerificationRepository,
    private val memberRegisterRepository: MemberRegisterRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
    private val fcmRepository: FcmRepository,
) : ViewModel() {

    private val _navigationDestination = MutableStateFlow<NavigationDestination?>(null)
    val navigationDestination = _navigationDestination.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    // 입력 상태값
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()
    private val _verificationCode = MutableStateFlow("")
    val verificationCode = _verificationCode.asStateFlow()
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    private val _dateOfBirth = MutableStateFlow("")
    val dateOfBirth = _dateOfBirth.asStateFlow()

    // 상태값
    private val _isMale = MutableStateFlow(true)
    val isMale = _isMale.asStateFlow()

    // Agreement state
    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    private val _checkedStates = MutableStateFlow(listOf(false, false))
    val checkedStates = _checkedStates.asStateFlow()

    private val _allAgreeCheckState = MutableStateFlow(false)
    val allAgreeCheckState = _allAgreeCheckState.asStateFlow()

    // 상태 변경
    fun onPhoneNumberChanged(new: String) {
        _phoneNumber.value = new
    }

    fun onVerificationCodeChanged(new: String) {
        _verificationCode.value = new
    }

    fun onNameChanged(new: String) {
        _name.value = new
    }

    fun onDOBChanged(new: String) {
        _dateOfBirth.value = new
    }

    fun onGenderChanged(new: Boolean) {
        _isMale.value = new
    }

    fun setShowBottomSheet(value: Boolean) {
        _showBottomSheet.value = value
    }

    fun setCheckedState(index: Int, value: Boolean) {
        _checkedStates.value = _checkedStates.value.toMutableList().apply { set(index, value) }
    }

    fun setAllAgreeCheckState(value: Boolean) {
        _allAgreeCheckState.value = value
        _checkedStates.value = List(_checkedStates.value.size) { value }
    }

    fun updateAllCheckedStates(newStates: List<Boolean>) {
        _checkedStates.value = newStates
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

    var isVerified = false
    var token = ""
        private set

    /** 인증번호 확인 및 로그인 처리 */
    fun confirmPhoneNumber(phone: String, code: String) {
        viewModelScope.launch {
            if (!debug) {
                verificationRepository.confirmPhoneNumber(phone, code)
                    .onSuccess {
                        Log.d("httplog", "로그인 성공, 액세스토큰: ${it.accessToken}")

                        isVerified = it.verified
                        if (isVerified) {
                            token = it.token ?: ""
                            dataStoreRepository.saveAccessToken(it.accessToken ?: "")
                            dataStoreRepository.saveRefreshToken(it.refreshToken ?: "")

                            // 로그인 후 FCM 토큰 유효성 검사
                            it.accessToken?.let { jwt ->
                                fcmRepository.validateAndRefreshTokenIfNeeded(jwt)
                                Log.d("httplog", "FCM 토큰 유효성 검사 및 갱신 완료")
                            }
                        }

                        if (it.memberStatus == "EXISTING_MEMBER")
                            _events.emit(LoginEvent.VerificationSuccessExisting)
                        else
                            _events.emit(LoginEvent.VerificationSuccessNew)
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
    fun memberRegister(name: String, birthDate: String, gender: GenderType) {
        viewModelScope.launch {
            try {
                if (debug) {
                    _events.emit(LoginEvent.MemberRegisterSuccess)
                    return@launch
                }

                // 🔹 이미 로그인 단계에서 FCM 토큰은 유효성 검증 완료됨
                val fcmToken = FirebaseMessaging.getInstance().token.await()
                Log.d("httplog", "회원가입 시 FCM 토큰 사용: $fcmToken")

                memberRegisterRepository.registerMember(
                    token = token,
                    name = name,
                    birthDate = birthDate.formatAsDate(),
                    gender = gender,
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
            _navigationDestination.value = destination
        }
    }

    fun onNavigationHandled() {
        _navigationDestination.value = null
    }
}
