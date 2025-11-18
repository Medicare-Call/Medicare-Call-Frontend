package com.konkuk.medicarecall.data.api.fcm

import retrofit2.Response
import retrofit2.http.POST

interface FcmValidationService {
    @POST("notifications/validation-token")
    suspend fun validateToken(): Response<Unit>
}
