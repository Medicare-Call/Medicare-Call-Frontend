package com.konkuk.medicarecall.data.api.member

import com.konkuk.medicarecall.data.dto.request.MemberRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MemberRegisterService {
    @POST("members")
    suspend fun postMemberRegister(
        @Header("Authorization") header: String,
        @Body request: MemberRegisterRequestDto,
    ): Response<MemberTokenResponseDto>
}
