package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.mapper.ElderInfoMapper
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.ElderInfo
import org.koin.core.annotation.Single

@Single
class UpdateElderInfoRepositoryImpl(
    private val eldersInfoService: EldersInfoService,
) : UpdateElderInfoRepository {
    override suspend fun updateElderInfo(elderInfo: ElderInfo): Result<Unit> = runCatching {
        val requestDto = ElderInfoMapper.toRequestDto(elderInfo)
        eldersInfoService.updateElder(elderInfo.elderId, requestDto).handleResponse()
    }

    override suspend fun deleteElder(id: Long): Result<Unit> = runCatching {
        eldersInfoService.deleteElderSettings(id).handleNullableResponse()
    }
}
