package com.konkuk.medicarecall.data.api.fcm

import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST

interface FcmUpdateService {
    @POST("member/fcm-token")
    suspend fun updateFcmToken(
        @Header("Authorization") header: String? = null, // Optional header
        @Body body: Map<String, String>, // {"fcmToken": "string"}
    ): Response<Unit>
}
