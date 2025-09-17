package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.VerificationResponseDto
import retrofit2.Response

interface VerificationRepository {
    suspend fun requestCertificationCode(phone: String): Result<Response<Unit>>
    suspend fun confirmPhoneNumber(phone: String, code: String): Result<VerificationResponseDto>
}
