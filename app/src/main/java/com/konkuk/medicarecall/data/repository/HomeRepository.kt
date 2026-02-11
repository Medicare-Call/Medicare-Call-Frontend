package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.HomeResponseDto

interface HomeRepository {
    suspend fun requestImmediateCareCall(elderId: Long, careCallOption: String): Result<Unit>
    suspend fun getHomeSummary(elderId: Long): Result<HomeResponseDto>
}
