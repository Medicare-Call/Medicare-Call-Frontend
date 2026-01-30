package com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel

sealed class LoginEvent {
    object VerificationSuccessNew : LoginEvent() // 인증번호 확인 성공(신규 회원)
    object VerificationSuccessExisting : LoginEvent() // 인증번호 확인 성공(기존 회원)

    object VerificationFailure : LoginEvent() // 인증번호 확인 실패
    object MemberRegisterSuccess : LoginEvent() // 회원가입 성공
    object MemberRegisterFailure : LoginEvent() // 회원가입 실패
}
