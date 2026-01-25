package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.payments.NaverPayService
import com.konkuk.medicarecall.data.dto.request.ReservePayRequestDto
import com.konkuk.medicarecall.data.dto.response.ReservePayResponseDto
import com.konkuk.medicarecall.data.repository.NaverPayRepository
import com.konkuk.medicarecall.data.util.handleResponse
import org.koin.core.annotation.Single

@Single
class NaverPayRepositoryImpl(
    private val naverPayService: NaverPayService,
) : NaverPayRepository {

    override suspend fun postReserveInfo(
        request: ReservePayRequestDto,
    ): Result<ReservePayResponseDto> = runCatching {
        naverPayService.postReservePay(request).handleResponse()
    }
}
