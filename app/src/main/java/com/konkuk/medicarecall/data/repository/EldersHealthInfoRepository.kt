package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto

interface EldersHealthInfoRepository {
    fun refresh()
    suspend fun getEldersHealthInfo(): Result<List<EldersHealthResponseDto>>
    suspend fun updateHealthInfo(elderInfo: EldersHealthResponseDto): Result<Unit>
}
