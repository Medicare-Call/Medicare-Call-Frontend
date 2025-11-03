package com.konkuk.medicarecall.data.api.payments

import com.konkuk.medicarecall.data.dto.request.ReservePayRequestDto
import com.konkuk.medicarecall.data.dto.response.ReservePayResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NaverPayService {
    // 결제창 호출은 아래와 같이 할 수 있습니다.
    // https://m.pay.naver.com/payments/{reserveId}
    @POST("payments/reserve")
    suspend fun postReservePay(
        @Body request: ReservePayRequestDto,
    ): Response<ReservePayResponseDto>
}
