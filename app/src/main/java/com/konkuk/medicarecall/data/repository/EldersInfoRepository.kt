package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.CallTimeResponseDto
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.domain.model.ElderInfo
import com.konkuk.medicarecall.ui.model.ElderSubscription

interface EldersInfoRepository {
    suspend fun getEldersV2(): Result<List<Elder>>
    suspend fun getElders(): Result<List<ElderInfo>>
    suspend fun getSubscriptions(): Result<List<ElderSubscription>>
    suspend fun updateElder(id: Long, request: ElderInfo): Result<Unit>
    suspend fun deleteElder(id: Long): Result<Unit>
    suspend fun getCareCallTimes(id: Long): Result<CallTimeResponseDto>
}
