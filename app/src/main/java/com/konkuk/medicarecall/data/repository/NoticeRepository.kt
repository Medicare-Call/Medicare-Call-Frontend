package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto

interface NoticeRepository {
    suspend fun getNotices(): Result<List<NoticesResponseDto>>
}
