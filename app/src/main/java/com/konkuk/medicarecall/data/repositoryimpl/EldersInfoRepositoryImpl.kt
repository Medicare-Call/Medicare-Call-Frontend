package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.dto.response.CallTimeResponseDto
import com.konkuk.medicarecall.data.mapper.ElderInfoMapper
import com.konkuk.medicarecall.data.mapper.toModels
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.domain.model.ElderInfo
import com.konkuk.medicarecall.ui.model.ElderSubscription
import org.koin.core.annotation.Single

@Single
class EldersInfoRepositoryImpl(
    private val eldersInfoService: EldersInfoService,
) : EldersInfoRepository {
    override suspend fun getElders(): Result<List<ElderInfo>> = runCatching {
        eldersInfoService.getElders().handleResponse().map { ElderInfoMapper.toDomain(it) }
    }

    override suspend fun getEldersV2(): Result<List<Elder>> = runCatching {
        eldersInfoService.getEldersV2().handleResponse().toModels()
    }

    override suspend fun getSubscriptions(): Result<List<ElderSubscription>> = runCatching {
        eldersInfoService.getSubscriptions().handleResponse().map { ElderInfoMapper.subscriptionToDomain(it) }
    }

    override suspend fun updateElder(
        id: Long,
        request: ElderInfo,
    ): Result<Unit> = runCatching {
        val requestDto = ElderInfoMapper.elderDataToRequestDto(request)
        eldersInfoService.updateElder(elderId = id, request = requestDto).handleResponse()
    }

    override suspend fun deleteElder(id: Long): Result<Unit> = runCatching {
        eldersInfoService.deleteElderSettings(id).handleNullableResponse()
    }

    override suspend fun getCareCallTimes(id: Long): Result<CallTimeResponseDto> = runCatching {
        eldersInfoService.getCallTimes(id).handleResponse()
    }
}
