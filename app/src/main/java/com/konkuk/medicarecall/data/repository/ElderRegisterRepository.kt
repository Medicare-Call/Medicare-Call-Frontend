package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderData

interface ElderRegisterRepository {
    suspend fun postElderBulk(elderList: List<LoginElderData>): Result<List<Elder>>
    suspend fun postElderHealthInfoBulk(elderHealthList: List<LoginElderData>): Result<Unit>
}
