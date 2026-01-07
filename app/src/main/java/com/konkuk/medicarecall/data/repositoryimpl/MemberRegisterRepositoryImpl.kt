package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.member.MemberRegisterService
import com.konkuk.medicarecall.data.dto.request.MemberRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import com.konkuk.medicarecall.data.repository.MemberRegisterRepository
import com.konkuk.medicarecall.ui.type.GenderType
import org.koin.core.annotation.Single

@Single
class MemberRegisterRepositoryImpl(
    private val memberRegisterService: MemberRegisterService,
) : MemberRegisterRepository {

    override suspend fun registerMember(
        token: String,
        name: String,
        birthDate: String,
        gender: GenderType,
        fcmToken: String,
    ): Result<MemberTokenResponseDto> =
        runCatching {
            val response = memberRegisterService.postMemberRegister(
                "Bearer $token",
                MemberRegisterRequestDto(
                    name,
                    birthDate,
                    gender,
                    fcmToken,
                ),
            )

            if (response.isSuccessful) {
                response.body() ?: error("Response body is null")
            } else {
                val errorBody = response.errorBody()?.toString() ?: "Unknown error"
                throw Exception(response.toString())
            }
        }
}
