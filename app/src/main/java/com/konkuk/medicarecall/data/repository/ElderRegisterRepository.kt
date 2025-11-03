package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.ElderBulkRegisterResponseDto
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.model.ElderHealthData

interface ElderRegisterRepository {
    suspend fun postElderHealthInfo(id: Int, elderHealthData: ElderHealthData)
    suspend fun postElderBulk(elderList: List<ElderData>): Result<ElderBulkRegisterResponseDto>
    suspend fun postElderHealthInfoBulk(elderHealthList: List<ElderHealthData>): Result<Unit>
}
