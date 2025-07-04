package com.konkuk.medicarecall.ui.login_info.uistate

sealed class LoginState {
    // 로그인 상태 확인
    object Loading : LoginState()
    object LoggedIn : LoginState() // 로그인됨
    object NotLoggedIn : LoginState() // 로그인안됨

}