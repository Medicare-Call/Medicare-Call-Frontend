package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.CallTimeResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.mapper.toModels
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.ui.common.util.formatAsDate
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.RelationshipType
import org.koin.core.annotation.Single

@Single
class EldersInfoRepositoryImpl(
    private val eldersInfoService: EldersInfoService,
) : EldersInfoRepository {
    override suspend fun getElders(): Result<List<EldersInfoResponseDto>> = runCatching {
        eldersInfoService.getElders().handleResponse()
    }

    override suspend fun getEldersV2(): Result<List<Elder>> = runCatching {
        eldersInfoService.getEldersV2().handleResponse().toModels()
    }

    override suspend fun getSubscriptions(): Result<List<EldersSubscriptionResponseDto>> = runCatching {
        eldersInfoService.getSubscriptions().handleResponse()
    }

    override suspend fun updateElder(
        id: Int,
        request: ElderData,
    ): Result<Unit> = runCatching {
        eldersInfoService.updateElder(
            elderId = id,
            request = ElderRegisterRequestDto(
                request.name,
                birthDate = request.birthDate.formatAsDate(),
                gender = if (request.gender) GenderType.MALE else GenderType.FEMALE,
                phone = request.phoneNumber,
                relationship = RelationshipType.entries.find { it.displayName == request.relationship }!!,
                residenceType = ElderResidenceType.entries.find { it.displayName == request.livingType }!!,
            ),
        ).handleResponse()
    }

    override suspend fun deleteElder(id: Int): Result<Unit> = runCatching {
        eldersInfoService.deleteElderSettings(id).handleNullableResponse()
    }

    override suspend fun getCareCallTimes(id: Int): Result<CallTimeResponseDto> = runCatching {
        eldersInfoService.getCallTimes(id).handleResponse()
    }
}
