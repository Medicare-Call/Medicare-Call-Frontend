package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.ElderRegisterService
import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.mapper.ElderHealthMapper
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.ElderHealthInfo
import org.koin.core.annotation.Single

@Single
class EldersHealthInfoRepositoryImpl(
    private val elderInfoService: EldersInfoService,
    private val elderRegisterService: ElderRegisterService,
) : EldersHealthInfoRepository {

    private var cachedHealthInfo: List<ElderHealthInfo>? = null

    override fun refresh() {
        cachedHealthInfo = null
    }

    override suspend fun getEldersHealthInfo(): Result<List<ElderHealthInfo>> {
        cachedHealthInfo?.let {
            return Result.success(it)
        }

        return runCatching {
            val domainModels = elderInfoService.getElderHealthInfo().handleResponse().map { ElderHealthMapper.toDomain(it) }
            cachedHealthInfo = domainModels // 캐시에 저장
            domainModels
        }
    }

    override suspend fun updateHealthInfo(
        elderHealthInfo: ElderHealthInfo,
    ): Result<Unit> = runCatching {
        val requestDto = ElderHealthMapper.toRequestDto(elderHealthInfo)
        elderRegisterService.postElderHealthInfo(elderHealthInfo.elderId, requestDto).handleNullableResponse()
        refresh()
    }
}
