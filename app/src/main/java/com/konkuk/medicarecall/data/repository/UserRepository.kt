package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto

interface UserRepository {
    suspend fun getMyInfo(): Result<MyInfoResponseDto>
    suspend fun updateMyInfo(userUpdateRequestDto: MyInfoResponseDto): Result<MyInfoResponseDto>
    suspend fun logout(): Result<Unit>
}
