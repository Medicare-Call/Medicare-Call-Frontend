package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.api.NaverPayService
import com.konkuk.medicarecall.data.dto.request.ReservePayRequestDto
import com.konkuk.medicarecall.data.dto.response.ReservePayResponseDto
import javax.inject.Inject

class NaverPayRepositoryImpl @Inject constructor(
    private val naverPayService: NaverPayService
) : NaverPayRepository {

    override suspend fun postReserveInfo(
        request: ReservePayRequestDto
    ): Result<ReservePayResponseDto> = runCatching {
        val response = naverPayService.postReservePay(request)
        if (response.isSuccessful) {
            response.body() ?: throw IllegalStateException("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            throw Exception("Error reserving payment: $errorBody / NaverPayRepository.kt")
        }
    }
}