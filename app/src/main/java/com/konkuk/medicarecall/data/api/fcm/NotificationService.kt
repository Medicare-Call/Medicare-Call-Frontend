package com.konkuk.medicarecall.data.api.fcm

import com.konkuk.medicarecall.data.dto.request.NotificationStatusRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationService { // 알림 관련
    @POST("notifications/{notificationId}")
    suspend fun changeStatus(
        @Path("notificationId") notificationId: String,
        @Body status: NotificationStatusRequestDto,
    ): Response<Unit>
}
