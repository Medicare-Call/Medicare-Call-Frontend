package com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel

import com.konkuk.medicarecall.domain.model.DoseStatus
import com.konkuk.medicarecall.domain.model.DoseStatusItem
import com.konkuk.medicarecall.domain.model.Medicine

data class MedicineUiState(
    val medicine: Medicine,
) {
    val displayDoseStatusList: List<DoseStatusItem>
        get() {
            val korMap = mapOf("MORNING" to "아침", "LUNCH" to "점심", "DINNER" to "저녁")

            // 1. 이름 변환 및 데이터 가공
            val convertedList = medicine.doseStatusList.map { item ->
                item.copy(time = korMap[item.time] ?: item.time)
            }

            // 2. 목표 횟수만큼 빈 칸 채우기
            val requiredCount = medicine.todayRequiredCount ?: 0
            return if (convertedList.size < requiredCount) {
                convertedList + List(requiredCount - convertedList.size) {
                    DoseStatusItem(time = "", doseStatus = DoseStatus.NOT_RECORDED)
                }
            } else {
                convertedList.take(requiredCount)
            }
        }
}
