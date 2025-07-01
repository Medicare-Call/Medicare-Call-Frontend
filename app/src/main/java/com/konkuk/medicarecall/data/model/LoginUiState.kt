package com.konkuk.medicarecall.data.model

sealed class LoginUiState {
    // 회원가입 단계
    object Start : LoginUiState()
    object EnterPhoneNumber : LoginUiState() // 휴대폰 인증 화면
    object EnterMyInfo : LoginUiState() // 회원 정보 화면
    object EnterSeniorInfo : LoginUiState() // 어르신 정보 등록 화면
    object Purchase : LoginUiState() // 결제, 케어콜 설정 화면
    object RegisterFinished : LoginUiState() // 완료 스플래시

}