package com.konkuk.medicarecall.data.repository

import android.util.Log
import com.konkuk.medicarecall.data.api.NoticeService
import com.konkuk.medicarecall.data.dto.response.NoticesResponseDto
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val noticeService: NoticeService
) : NoticeRepository {
    override suspend fun getNotices(): Result<List<NoticesResponseDto>> {
        Log.d("NoticeRepository", "공지사항 불러오기 시작(getNotices() 호출됨)")
        return runCatching {
            val response = noticeService.getNotices()
            Log.d("NoticeRepository", "응답 수신됨: isSuccessful = ${response.isSuccessful}")
            Log.d("NoticeRepository", "공지사항 응답 코드: ${response.code()}")
            if (response.isSuccessful) {
                val body = response.body() ?: throw NullPointerException("Response body is null")
                Log.d("NoticeRepository", "응답 바디: ${body.size}개")
                body
            } else {
                val error = response.errorBody()?.string()
                Log.e("NoticeRepository", "응답 실패: $error")
                throw Exception("Error fetching notices: ${response.errorBody()?.string()}")
            }
        }.onFailure {
            Log.e("NoticeRepository", "공지사항 불러오기 실패", it)
        }
    }
}
