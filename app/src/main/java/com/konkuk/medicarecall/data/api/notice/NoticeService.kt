package com.konkuk.medicarecall.data.api.notice

import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface NoticeService {
    // 공지사항 조회
    @GET("notices")
    suspend fun getNotices(): Response<List<NoticesResponseDto>>
}
