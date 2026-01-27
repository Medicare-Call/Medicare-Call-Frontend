package com.konkuk.medicarecall.data.api.fcm

import com.konkuk.medicarecall.data.dto.request.NotificationStatusRequestDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface NotificationService { // 알림 관련
    @POST("notifications/{notificationId}")
    suspend fun changeStatus(
        @Path("notificationId") notificationId: String,
        @Body status: NotificationStatusRequestDto,
    ): Response<Unit>
}
