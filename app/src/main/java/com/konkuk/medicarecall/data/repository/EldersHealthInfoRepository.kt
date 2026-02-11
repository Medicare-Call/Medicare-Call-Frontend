package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.ElderHealthInfo

interface EldersHealthInfoRepository {
    fun refresh()
    suspend fun getEldersHealthInfo(): Result<List<ElderHealthInfo>>
    suspend fun updateHealthInfo(elderHealthInfo: ElderHealthInfo): Result<Unit>
}
