package com.konkuk.medicarecall.data.api.member

import com.konkuk.medicarecall.data.dto.request.MemberRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.MemberTokenResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface MemberRegisterService {
    @POST("members")
    suspend fun postMemberRegister(
        @Body request: MemberRegisterRequestDto,
    ): Response<MemberTokenResponseDto>
}
