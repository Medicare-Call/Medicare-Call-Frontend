package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.CallTimeResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.model.ElderInfo
import com.konkuk.medicarecall.ui.model.ElderSubscription

interface EldersInfoRepository {
    suspend fun getEldersV2(): Result<List<Elder>>
    suspend fun getElders(): Result<List<ElderInfo>>
    suspend fun getSubscriptions(): Result<List<ElderSubscription>>
    suspend fun updateElder(id: Int, request: ElderData): Result<Unit>
    suspend fun deleteElder(id: Int): Result<Unit>
    suspend fun getCareCallTimes(id: Int): Result<CallTimeResponseDto>
}
