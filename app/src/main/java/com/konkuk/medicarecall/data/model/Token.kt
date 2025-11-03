package com.konkuk.medicarecall.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val fcmAccessToken: String? = null, // FCM 관련 서버 API 호출 시 사용되는 인증 토큰
    val fcmToken: String? = null, // fcm 등록 토큰(fcm서비스가 발급해준 고유의 토큰)
)
