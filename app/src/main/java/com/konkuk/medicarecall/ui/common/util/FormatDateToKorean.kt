package com.konkuk.medicarecall.ui.common.util

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * @param dateStr yyyy-MM-dd
 * @return yyyy년 M월 d일
 */
fun formatDateToKorean(dateStr: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)
        val date = inputFormat.parse(dateStr)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        dateStr // 파싱 실패 시 원래 문자열 반환
    }
}
