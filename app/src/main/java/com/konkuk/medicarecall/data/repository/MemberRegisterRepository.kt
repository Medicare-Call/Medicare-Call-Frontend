package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import com.konkuk.medicarecall.ui.type.GenderType

interface MemberRegisterRepository {
    suspend fun registerMember(
        name: String,
        birthDate: String,
        gender: GenderType,
        fcmToken: String,
    ): Result<MemberTokenResponseDto>
}
