package com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel

data class HealthUiState(
    val symptoms: List<String>, // 건강 징후 리스트 (예: ["손 떨림 증상", "거동 불편", "몸이 느려짐"])
    val symptomAnalysis: String, // 증상 분석 결과 문장 (예: "파킨슨 병이 의심돼요. 병원 방문 권장")
    val isRecorded: Boolean, // 데이터 기록 여부 (예: true = 기록됨, false = 기록 안 됨)
) {
    companion object {
        val EMPTY = HealthUiState(
            symptoms = emptyList(),
            symptomAnalysis = "",
            isRecorded = false,
        )
    }
}
