package com.konkuk.medicarecall.data.api.auth

import com.konkuk.medicarecall.data.dto.request.CertificationCodeRequestDto
import com.konkuk.medicarecall.data.dto.request.PhoneNumberConfirmRequestDto
import com.konkuk.medicarecall.data.dto.response.VerificationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/** 인증과 관련된 API 모음 (토큰 갱신, 본인인증, 로그아웃) */

interface AuthService {
    @POST("verifications")
    suspend fun requestCertificationCode(@Body req: CertificationCodeRequestDto): Response<Unit>

    @POST("verifications/confirmation")
    suspend fun confirmPhoneNumber(@Body req: PhoneNumberConfirmRequestDto): Response<VerificationResponseDto>

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") authorization: String, // "Bearer <refresh>"
    ): Response<Unit>
}
