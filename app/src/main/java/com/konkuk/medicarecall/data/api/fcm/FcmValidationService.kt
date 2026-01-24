package com.konkuk.medicarecall.data.api.fcm

import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.POST

interface FcmValidationService {
    @POST("notifications/validation-token")
    suspend fun validateToken(): Response<Unit>
}
