package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.request.ReservePayRequestDto
import com.konkuk.medicarecall.data.dto.response.ReservePayResponseDto

interface NaverPayRepository {
    suspend fun postReserveInfo(request: ReservePayRequestDto): Result<ReservePayResponseDto>
}
