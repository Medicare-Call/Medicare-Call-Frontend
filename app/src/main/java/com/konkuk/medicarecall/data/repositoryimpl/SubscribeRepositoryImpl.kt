package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.SubscribeService
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import com.konkuk.medicarecall.data.util.handleResponse
import org.koin.core.annotation.Single

@Single
class SubscribeRepositoryImpl(
    private val subscribeService: SubscribeService,
) : SubscribeRepository {
    override suspend fun getSubscriptions(): Result<List<EldersSubscriptionResponseDto>> = runCatching {
        subscribeService.getElderSubscriptions().handleResponse()
    }
}
