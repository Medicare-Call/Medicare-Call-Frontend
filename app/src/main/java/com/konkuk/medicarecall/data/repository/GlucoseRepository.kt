package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.GlucoseResponseDto

interface GlucoseRepository {
    suspend fun getGlucoseGraph(elderId: Long, counter: Int, type: String): Result<GlucoseResponseDto>
}
