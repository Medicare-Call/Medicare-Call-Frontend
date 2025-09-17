package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.model.ElderHealthData

interface ElderRegisterRepository {
    suspend fun postElderHealthInfo(id: Int, elderHealthData: ElderHealthData)
    suspend fun registerElderAndHealth(
        elders: Int,
        elderInfoList: List<ElderData>,
        elderHealthInfo: List<ElderHealthData>
    ): Result<Unit>
}
