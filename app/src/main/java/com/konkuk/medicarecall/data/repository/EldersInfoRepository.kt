package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.CallTimeResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.ui.model.ElderData

interface EldersInfoRepository {
    suspend fun getElders(): Result<List<EldersInfoResponseDto>>
    suspend fun getEldersV2(): Result<List<Elder>>
    suspend fun getSubscriptions(): Result<List<EldersSubscriptionResponseDto>>
    suspend fun updateElder(id: Int, request: ElderData): Result<Unit>
    suspend fun deleteElder(id: Int): Result<Unit>
    suspend fun getCareCallTimes(id: Int): Result<CallTimeResponseDto>
}
