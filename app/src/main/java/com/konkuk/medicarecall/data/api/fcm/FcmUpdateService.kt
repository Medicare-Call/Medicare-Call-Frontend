package com.konkuk.medicarecall.data.api.fcm

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmUpdateService {
    @POST("member/fcm-token")
    suspend fun updateFcmToken(
        @Header("Authorization") header: String? = null, // Optional header
        @Body body: Map<String, String>, // {"fcmToken": "string"}
    ): Response<Unit>
}
