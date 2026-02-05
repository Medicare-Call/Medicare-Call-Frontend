package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Verification
import de.jensklingenberg.ktorfit.Response

interface VerificationRepository {
    suspend fun requestCertificationCode(phone: String): Result<Response<Unit>>
    suspend fun confirmPhoneNumber(phone: String, code: String): Result<Verification>
}
