package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.api.MemberRegisterService
import com.konkuk.medicarecall.data.dto.request.MemberRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import com.konkuk.medicarecall.ui.model.GenderType
import retrofit2.HttpException
import javax.inject.Inject

class MemberRegisterRepositoryImpl @Inject constructor(
    private val memberRegisterService: MemberRegisterService
) : MemberRegisterRepositoryInterface {
    override suspend fun registerMember(
        token: String,
        name: String,
        birthDate: String,
        gender: GenderType
    ): Result<MemberTokenResponseDto> =
        runCatching {
            val response = memberRegisterService.postMemberRegister(
                "Bearer $token",
                MemberRegisterRequestDto(
                    name,
                    birthDate,
                    gender
                )
            )

            if (response.isSuccessful) {
                response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                throw HttpException(response)
            }
        }

}