package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.model.ElderSubscription

interface SubscribeRepository {
    suspend fun getSubscriptions(): Result<List<ElderSubscription>>
}
