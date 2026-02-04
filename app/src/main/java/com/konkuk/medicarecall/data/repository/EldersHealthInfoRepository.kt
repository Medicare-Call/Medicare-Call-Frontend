package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.model.ElderHealthInfo

interface EldersHealthInfoRepository {
    fun refresh()
    suspend fun getEldersHealthInfo(): Result<List<ElderHealthInfo>>
    suspend fun updateHealthInfo(elderHealthInfo: ElderHealthInfo): Result<Unit>
}
