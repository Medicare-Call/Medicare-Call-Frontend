package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.SubscribeService
import com.konkuk.medicarecall.data.mapper.ElderInfoMapper
import com.konkuk.medicarecall.data.repository.SubscribeRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.ui.model.ElderSubscription
import org.koin.core.annotation.Single

@Single
class SubscribeRepositoryImpl(
    private val subscribeService: SubscribeService,
) : SubscribeRepository {
    override suspend fun getSubscriptions(): Result<List<ElderSubscription>> = runCatching {
        subscribeService.getElderSubscriptions().handleResponse().map {
            ElderInfoMapper.subscriptionToDomain(it)
        }
    }
}
