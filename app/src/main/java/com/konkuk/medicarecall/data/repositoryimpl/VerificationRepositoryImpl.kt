package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.auth.AuthService
import com.konkuk.medicarecall.data.dto.request.CertificationCodeRequestDto
import com.konkuk.medicarecall.data.dto.request.PhoneNumberConfirmRequestDto
import com.konkuk.medicarecall.data.dto.response.VerificationResponseDto
import com.konkuk.medicarecall.data.repository.VerificationRepository
import retrofit2.HttpException
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(
    private val authService: AuthService,
) : VerificationRepository {
    override suspend fun requestCertificationCode(phone: String) =
        runCatching { authService.requestCertificationCode(CertificationCodeRequestDto(phone)) }

    override suspend fun confirmPhoneNumber(phone: String, code: String): Result<VerificationResponseDto> =
        runCatching {
            val response = authService.confirmPhoneNumber(
                PhoneNumberConfirmRequestDto(phone, code),
            )

            if (response.isSuccessful) {
                response.body() ?: error("Response body is null")
            } else {
                throw HttpException(response)
            }
        }
}
