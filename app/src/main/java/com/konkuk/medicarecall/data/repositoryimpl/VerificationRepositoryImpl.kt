package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.auth.AuthService
import com.konkuk.medicarecall.data.dto.request.CertificationCodeRequestDto
import com.konkuk.medicarecall.data.dto.request.PhoneNumberConfirmRequestDto
import com.konkuk.medicarecall.data.dto.response.VerificationResponseDto
import com.konkuk.medicarecall.data.repository.VerificationRepository
import com.konkuk.medicarecall.data.util.handleResponse
import org.koin.core.annotation.Single

@Single
class VerificationRepositoryImpl(
    private val authService: AuthService,
) : VerificationRepository {
    override suspend fun requestCertificationCode(phone: String) =
        runCatching { authService.requestCertificationCode(CertificationCodeRequestDto(phone)) }

    override suspend fun confirmPhoneNumber(phone: String, code: String): Result<VerificationResponseDto> =
        runCatching {
            authService.confirmPhoneNumber(PhoneNumberConfirmRequestDto(phone, code)).handleResponse()
        }
}
