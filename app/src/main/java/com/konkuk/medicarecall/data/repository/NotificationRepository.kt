package com.konkuk.medicarecall.data.repository

interface NotificationRepository {
    suspend fun changeNotificationState(id: String): Result<Unit>
    suspend fun testNotification(): Result<Unit>
}
