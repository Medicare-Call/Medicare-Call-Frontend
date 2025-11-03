package com.konkuk.medicarecall.ui.feature.statistics.viewmodel

/**
 * 주간 요약 통계 화면의 전체 상태를 담는 데이터 클래스
 * - 식사율, 복약율, 건강징후, 미응답
 * - 식사/복약 통계 리스트
 * - 건강징후 요약 메시지
 * - 평균 수면 시간
 * - 심리 상태 통계
 * - 혈당 공복/식후 통계
 */

data class WeeklySummaryUiState(
    val elderName: String = "",
    val weeklyMealRate: Int,
    val weeklyMedicineRate: Int,
    val weeklyHealthIssueCount: Int,
    val weeklyUnansweredCount: Int,

    val weeklyMeals: List<WeeklyMealUiState> = emptyList(),
    val weeklyMedicines: List<WeeklyMedicineUiState> = emptyList(),
    val weeklyHealthNote: String = "",
    val weeklySleepHours: Int? = 0,
    val weeklySleepMinutes: Int? = 0,
    val weeklySleepRecorded: Boolean = true,
    val weeklyMental: WeeklyMentalUiState = WeeklyMentalUiState.EMPTY,
    val weeklyGlucose: WeeklyGlucoseUiState = WeeklyGlucoseUiState.EMPTY,
) {
    companion object {

        val EMPTY = WeeklySummaryUiState(
            elderName = "",
            weeklyMealRate = -1,
            weeklyMedicineRate = -1,
            weeklyHealthIssueCount = -1,
            weeklyUnansweredCount = -1,
            weeklyMeals = listOf(
                WeeklyMealUiState("아침", -1, 7),
                WeeklyMealUiState("점심", -1, 7),
                WeeklyMealUiState("저녁", -1, 7),
            ),
            weeklyMedicines = emptyList(),
            weeklyHealthNote = "아직 충분한 기록이 쌓이지 않았어요.",
            weeklySleepHours = null,
            weeklySleepMinutes = null,
            weeklyMental = WeeklyMentalUiState.EMPTY,
            weeklyGlucose = WeeklyGlucoseUiState.EMPTY,
        )
    }
}

/**
 * ✅ 아침/점심/저녁 식사 통계용
 * ex) 아침 7/7, 점심 5/7, 저녁 1/7
 */
data class WeeklyMealUiState(
    val mealType: String, // 예: "아침"
    val eatenCount: Int?, // 실제 완료 횟수
    val totalCount: Int, // 총 예정 횟수 (보통 7)
)

/**
 * ✅ 복약 통계용
 * ex) 혈압약 0/14, 영양제 4/7, 당뇨약 21/21
 */
data class WeeklyMedicineUiState(
    val medicineName: String, // 예: "혈압약"
    val takenCount: Int?, // 완료 횟수
    val totalCount: Int, // 총 예정 횟수
)

/**
 * ✅ 심리 상태 통계용
 * ex) 좋음 4, 보통 4, 나쁨 4
 */
data class WeeklyMentalUiState(
    val good: Int, // 좋음 횟수
    val normal: Int, // 보통 횟수
    val bad: Int, // 나쁨 횟수
) {

    companion object {
        val EMPTY = WeeklyMentalUiState(
            good = -1,
            normal = -1,
            bad = -1,
        )
    }
}

/**
 * ✅ 혈당 공복/식후 통계용
 * ex) 공복: 정상 5, 높음 2, 낮음 1
 * 식후: 정상 5, 높음 0, 낮음 2
 */
data class WeeklyGlucoseUiState(
    val beforeMealNormal: Int, // 공복 정상 횟수
    val beforeMealHigh: Int, // 공복 높음 횟수
    val beforeMealLow: Int, // 공복 낮음 횟수

    val afterMealNormal: Int, // 식후 정상 횟수
    val afterMealHigh: Int, // 식후 높음 횟수
    val afterMealLow: Int, // 식후 낮음 횟수
) {
    companion object {
        val EMPTY = WeeklyGlucoseUiState(
            beforeMealNormal = 0,
            beforeMealHigh = 0,
            beforeMealLow = 0,
            afterMealNormal = 0,
            afterMealHigh = 0,
            afterMealLow = 0,
        )
    }
}
