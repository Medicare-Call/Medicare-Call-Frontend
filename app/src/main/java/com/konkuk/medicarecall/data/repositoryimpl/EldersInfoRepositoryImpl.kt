package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.dto.response.CallTimeResponseDto
import com.konkuk.medicarecall.data.mapper.ElderInfoMapper
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.model.ElderInfo
import com.konkuk.medicarecall.ui.model.ElderSubscription
import org.koin.core.annotation.Single

@Single
class EldersInfoRepositoryImpl(
    private val eldersInfoService: EldersInfoService,
) : EldersInfoRepository {
    override suspend fun getElders(): Result<List<ElderInfo>> = runCatching {
        val responseDto = eldersInfoService.getElders().handleResponse()
        responseDto.map { ElderInfoMapper.toDomain(it) }
    }

    override suspend fun getSubscriptions(): Result<List<ElderSubscription>> = runCatching {
        val responseDto = eldersInfoService.getSubscriptions().handleResponse()
        responseDto.map { ElderInfoMapper.subscriptionToDomain(it) }
    }

    override suspend fun updateElder(
        id: Int,
        request: ElderData,
    ): Result<Unit> = runCatching {
        val requestDto = ElderInfoMapper.elderDataToRequestDto(request)
        eldersInfoService.updateElder(elderId = id, request = requestDto).handleResponse()
    }

    override suspend fun deleteElder(id: Int): Result<Unit> = runCatching {
        eldersInfoService.deleteElderSettings(id).handleNullableResponse()
    }

    override suspend fun getCareCallTimes(id: Int): Result<CallTimeResponseDto> = runCatching {
        eldersInfoService.getCallTimes(id).handleResponse()
    }
}
