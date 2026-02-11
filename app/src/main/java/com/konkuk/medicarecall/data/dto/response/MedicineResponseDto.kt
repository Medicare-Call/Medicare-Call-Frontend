package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MedicineResponseDto(
    val date: String,
    val medications: List<MedicineStatusDto>,
)

@Serializable
data class MedicineStatusDto(
    val type: String, // 약 종류 (예: "당뇨약")
    val goalCount: Int, // 하루 목표 복약 횟수
    val takenCount: Int, // 복약 완료 횟수
    val times: List<MedicineTimeDto>, // 복용 시간대 별 상태
)

@Serializable
data class MedicineTimeDto(
    val time: String, // 복약 시간대 (예: "MORNING", "LUNCH", "DINNER")
    val taken: Boolean?, // 복용 여부
)

@Serializable
data class MedicineInfoDto(
    val id: Int,
    val name: String,
    val dosage: String,
    val timesPerDay: Int,
)
