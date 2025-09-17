package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.VerificationService
import com.konkuk.medicarecall.data.dto.request.CertificationCodeRequestDto
import com.konkuk.medicarecall.data.dto.request.PhoneNumberConfirmRequestDto
import com.konkuk.medicarecall.data.dto.response.VerificationResponseDto
import com.konkuk.medicarecall.data.repository.VerificationRepository
import retrofit2.HttpException
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(
    private val verificationService: VerificationService,
) : VerificationRepository {
    override suspend fun requestCertificationCode(phone: String) =
        runCatching { verificationService.requestCertificationCode(CertificationCodeRequestDto(phone)) }

    override suspend fun confirmPhoneNumber(phone: String, code: String): Result<VerificationResponseDto> =
        runCatching {
            val response = verificationService.confirmPhoneNumber(
                PhoneNumberConfirmRequestDto(phone, code),
            )

            if (response.isSuccessful) {
                response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                throw HttpException(response)
            }
        }
}
