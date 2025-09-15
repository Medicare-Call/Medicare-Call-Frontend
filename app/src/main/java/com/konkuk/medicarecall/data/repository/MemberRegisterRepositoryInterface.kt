package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import com.konkuk.medicarecall.ui.model.GenderType

interface MemberRegisterRepositoryInterface {
    suspend fun registerMember(
        token: String,
        name: String,
        birthDate: String,
        gender: GenderType
    ): Result<MemberTokenResponseDto>
}