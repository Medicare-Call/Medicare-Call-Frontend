package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.HomeService
import com.konkuk.medicarecall.data.dto.request.ImmediateCallRequestDto
import com.konkuk.medicarecall.data.mapper.toHome
import com.konkuk.medicarecall.data.repository.HomeRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Home
import org.koin.core.annotation.Single

@Single
class HomeRepositoryImpl(
    private val homeService: HomeService,
) : HomeRepository {
    override suspend fun requestImmediateCareCall(
        elderId: Long,
        careCallOption: String,
    ): Result<Unit> = runCatching {
        homeService.requestImmediateCareCall(
            ImmediateCallRequestDto(elderId, careCallOption),
        ).handleNullableResponse()
    }

    override suspend fun getHomeSummary(elderId: Long): Result<Home> = runCatching {
        homeService.getHomeSummary(elderId).handleResponse().toHome()
    }
}
