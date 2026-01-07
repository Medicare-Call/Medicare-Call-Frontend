package com.konkuk.medicarecall.data.api.auth

import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST

interface RefreshService {
    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Refresh-Token") header: String): Response<MemberTokenResponseDto>
}
