package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.SubscribeService
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import retrofit2.HttpException
import javax.inject.Inject

class SubscribeRepositoryImpl @Inject constructor(
    private val subscribeService: SubscribeService,
) : SubscribeRepository {
    override suspend fun getSubscriptions(): Result<List<EldersSubscriptionResponseDto>> {
        Log.d("SubscribeRepository", "구독 정보 불러오기 시작(getSubscriptions() 호출됨)")
        return runCatching {
            val response = subscribeService.getElderSubscriptions()
            Log.d("SubscribeRepository", "응답 수신됨: isSuccessful = ${response.isSuccessful}")
            Log.d("SubscribeRepository", "구독 정보 응답 코드: ${response.code()}")
            if (response.isSuccessful) {
                val body = response.body() ?: throw NullPointerException("Response body is null")
                Log.d("SubscribeRepository", "응답 바디: ${body.size}개")
                body
            } else {
                val error = response.errorBody()?.string()
                Log.e("SubscribeRepository", "응답 실패: $error")
                throw HttpException(response)
            }
        }.onFailure {
            Log.e("SubscribeRepository", "구독 정보 불러오기 실패", it)
        }
    }
}
