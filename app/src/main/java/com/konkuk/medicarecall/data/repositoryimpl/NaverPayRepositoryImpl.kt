package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.payments.NaverPayService
import com.konkuk.medicarecall.data.dto.request.ReservePayRequestDto
import com.konkuk.medicarecall.data.dto.response.ReservePayResponseDto
import com.konkuk.medicarecall.data.repository.NaverPayRepository
import retrofit2.HttpException
import javax.inject.Inject

class NaverPayRepositoryImpl @Inject constructor(
    private val naverPayService: NaverPayService,
) : NaverPayRepository {

    override suspend fun postReserveInfo(
        request: ReservePayRequestDto,
    ): Result<ReservePayResponseDto> = runCatching {
        val response = naverPayService.postReservePay(request)
        if (response.isSuccessful) {
            response.body() ?: error("Response body is null")
        } else {
            throw HttpException(response)
        }
    }
}
