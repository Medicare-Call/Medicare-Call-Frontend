package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.notice.NoticeService
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import com.konkuk.medicarecall.data.repository.NoticeRepository
import com.konkuk.medicarecall.data.util.handleResponse
import org.koin.core.annotation.Single

@Single
class NoticeRepositoryImpl(
    private val noticeService: NoticeService,
) : NoticeRepository {
    override suspend fun getNotices(): Result<List<NoticesResponseDto>> = runCatching {
        noticeService.getNotices().handleResponse()
    }
}
