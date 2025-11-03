package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class GlucoseGraphResponseDto(
    val period: PeriodDto,
    val data: List<GlucoseDayValueDto>,
    val average: GlucoseStatDto? = null, // 하루 평균 (nullable)
    val latest: GlucoseStatDto? = null, // 가장 최근 값 (nullable)
)

@Serializable
data class PeriodDto(
    val startDate: String, // 조회 시작일 (yyyy-MM-dd)
    val endDate: String, // 조회 종료일 (yyyy-MM-dd)
)

@Serializable
data class GlucoseDayValueDto(
    val date: String, // 측정 날짜 (yyyy-MM-dd)
    val value: Int, // 혈당 수치 (mg/dL)
    val status: GlucoseStatus, // LOW, NORMAL, HIGH
)

@Serializable
data class GlucoseStatDto(
    val value: Int, // 수치
    val status: GlucoseStatus, // LOW, NORMAL, HIGH
)

@Serializable
enum class GlucoseStatus { LOW, NORMAL, HIGH }
