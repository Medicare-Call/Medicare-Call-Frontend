package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.dto.request.ElderBulkHealthInfoRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderBulkRegisterRequestDto
import com.konkuk.medicarecall.data.mapper.toElderBulkRequestDto
import com.konkuk.medicarecall.data.mapper.toElderHealthBulkRequestDto
import com.konkuk.medicarecall.data.mapper.toModel
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderData
import org.koin.core.annotation.Single

@Single
class ElderRegisterRepositoryImpl(
    private val elderRegisterService: ElderRegisterService,
) : ElderRegisterRepository {

    override suspend fun postElderBulk(elderList: List<LoginElderData>): Result<List<Elder>> = runCatching {
        val request = ElderBulkRegisterRequestDto(
            elders = elderList.map { it.toModel().toElderBulkRequestDto() },
        )
        elderRegisterService.postElderBulk(request).handleResponse().toModel()
    }

    override suspend fun postElderHealthInfoBulk(elderHealthList: List<LoginElderData>): Result<Unit> = runCatching {
        val request = ElderBulkHealthInfoRequestDto(
            healthInfos = elderHealthList.map { it.toModel().toElderHealthBulkRequestDto() },
        )
        elderRegisterService.postElderHealthInfoBulk(request).handleNullableResponse()
    }
}
