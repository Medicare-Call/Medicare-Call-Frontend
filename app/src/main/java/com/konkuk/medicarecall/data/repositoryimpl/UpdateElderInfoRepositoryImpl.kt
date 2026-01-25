package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import com.konkuk.medicarecall.data.util.handleResponse
import org.koin.core.annotation.Single

@Single
class UpdateElderInfoRepositoryImpl(
    private val eldersInfoService: EldersInfoService,
) : UpdateElderInfoRepository {
    override suspend fun updateElderInfo(id: Int, request: ElderRegisterRequestDto): Result<Unit> =
        runCatching {
            eldersInfoService.updateElder(id, request).handleResponse()
        }

    override suspend fun deleteElder(id: Int): Result<Unit> = runCatching {
        eldersInfoService.deleteElderSettings(id).handleResponse()
    }
}
