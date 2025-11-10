package com.konkuk.medicarecall.data.api.auth

import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshService {
    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Refresh-Token") header: String): Response<MemberTokenResponseDto>
}
