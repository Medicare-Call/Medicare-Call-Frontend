package com.konkuk.medicarecall.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FcmToken(
    val fcmToken: String? = null, // fcm 등록 토큰(fcm서비스가 발급해준 고유의 토큰)
)
