package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.member.MemberRegisterService
import com.konkuk.medicarecall.data.dto.request.MemberRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import com.konkuk.medicarecall.data.repository.MemberRegisterRepository
import com.konkuk.medicarecall.ui.type.GenderType
import retrofit2.HttpException
import javax.inject.Inject

class MemberRegisterRepositoryImpl @Inject constructor(
    private val memberRegisterService: MemberRegisterService,
) : MemberRegisterRepository {
    override suspend fun registerMember(
        token: String,
        name: String,
        birthDate: String,
        gender: GenderType,
    ): Result<MemberTokenResponseDto> =
        runCatching {
            val response = memberRegisterService.postMemberRegister(
                "Bearer $token",
                MemberRegisterRequestDto(
                    name,
                    birthDate,
                    gender,
                ),
            )

            if (response.isSuccessful) {
                response.body() ?: error("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                throw HttpException(response)
            }
        }
}
