package com.konkuk.medicarecall.data.api.member

import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SettingService {
    // 설정 불러오기
    @GET("member")
    suspend fun getMyInfo(): Response<MyInfoResponseDto>

    // 내 정보 수정
    @POST("member")
    suspend fun updateMyInfo(
        @Body userUpdateRequestDto: MyInfoResponseDto,
    ): Response<MyInfoResponseDto> // 추후 수정 필요
}
