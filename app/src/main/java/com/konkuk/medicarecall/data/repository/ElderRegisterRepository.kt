package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.ElderBulkRegisterResponseDto
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderData

interface ElderRegisterRepository {
    suspend fun postElderBulk(elderList: List<LoginElderData>): Result<ElderBulkRegisterResponseDto>
    suspend fun postElderHealthInfoBulk(elderHealthList: List<LoginElderData>): Result<Unit>
}
